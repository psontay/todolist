package com.sontaypham.todolist.integration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sontaypham.todolist.DTO.Request.TaskCreationRequest;
import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.DTO.Response.TaskStatisticsResponse;
import com.sontaypham.todolist.Entities.Task;
import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Enums.TaskStatus;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Mapper.TaskMapper;
import com.sontaypham.todolist.Repository.TaskRepository;
import com.sontaypham.todolist.Service.CurrentUserService;
import com.sontaypham.todolist.Service.TaskService;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class TaskServiceIT {

  @InjectMocks TaskService taskService;

  @Mock TaskRepository taskRepository;
  @Mock TaskMapper taskMapper;
  @Mock CurrentUserService currentUserService;

  @Mock TaskResponse taskResponse;

  User mockUser;
  Task mockTask;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockUser = User.builder().id("user1").name("Test").tasks(new HashSet<>()).build();
    mockTask =
        Task.builder()
            .id("task1")
            .title("Task Title")
            .status(TaskStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .user(mockUser)
            .build();
    mockUser.getTasks().add(mockTask);
  }

  @Test
  void create_success() {
    TaskCreationRequest request = new TaskCreationRequest();
    request.setTitle("New Task");

    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTask(request)).thenReturn(mockTask);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    TaskResponse response = taskService.create(request);

    assertNotNull(response);
    verify(taskRepository).save(mockTask);
    verify(taskMapper).toTaskResponse(mockTask);
  }

  @Test
  void getAllTasksOfCurrentUser_success() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    List<TaskResponse> result = taskService.getAllTasksOfCurrentUser();
    assertEquals(1, result.size());
  }

  @Test
  void getTaskById_success() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    TaskResponse result = taskService.getTaskById("task1");
    assertNotNull(result);
  }

  @Test
  void getTaskById_notFound_throwsException() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    mockUser.getTasks().clear();

    ApiException ex = assertThrows(ApiException.class, () -> taskService.getTaskById("invalid"));
    assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void updateTask_success() {
    TaskUpdateRequest request =
        TaskUpdateRequest.builder().title("Updated").status(TaskStatus.COMPLETED).build();

    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    TaskResponse result = taskService.updateTask("task1", request);

    assertEquals("Updated", mockTask.getTitle());
    assertEquals(TaskStatus.COMPLETED, mockTask.getStatus());
    verify(taskRepository).save(mockTask);
  }

  @Test
  void updateTask_notFound_throwsException() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    mockUser.getTasks().clear();

    ApiException ex =
        assertThrows(
            ApiException.class, () -> taskService.updateTask("invalid", new TaskUpdateRequest()));
    assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void deleteTask_success() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);

    taskService.deleteTask("task1");

    assertFalse(mockUser.getTasks().contains(mockTask));
    verify(taskRepository).delete(mockTask);
  }

  @Test
  void deleteTask_notFound_throwsException() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    mockUser.getTasks().clear();

    ApiException ex = assertThrows(ApiException.class, () -> taskService.deleteTask("invalid"));
    assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void getTasksByStatus_success() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    List<TaskResponse> result = taskService.getTasksByStatus(TaskStatus.PENDING);
    assertEquals(1, result.size());
  }

  @Test
  void searchTasks_success() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    List<TaskResponse> result = taskService.searchTasks("task");
    assertEquals(1, result.size());
  }

  @Test
  void searchTasks_noMatch_returnsEmpty() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);

    List<TaskResponse> result = taskService.searchTasks("unmatch");
    assertEquals(0, result.size());
  }

  @Test
  void getStatistics_success() {
    when(currentUserService.getCurrentUser()).thenReturn(mockUser);

    TaskStatisticsResponse result = taskService.getStatistics();
    assertEquals(1, result.getTotal());
    assertEquals(1, result.getPending());
    assertEquals(0, result.getCompleted());
  }
}
