package com.sontaypham.todolist.integration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sontaypham.todolist.dto.request.TaskCreationRequest;
import com.sontaypham.todolist.dto.request.TaskUpdateRequest;
import com.sontaypham.todolist.dto.response.TaskResponse;
import com.sontaypham.todolist.dto.response.TaskStatisticsResponse;
import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.TaskStatus;
import com.sontaypham.todolist.exception.ApiException;
import com.sontaypham.todolist.exception.ErrorCode;
import com.sontaypham.todolist.mapper.TaskMapper;
import com.sontaypham.todolist.repository.TaskRepository;
import com.sontaypham.todolist.service.impl.CurrentUserServiceImpl;
import com.sontaypham.todolist.service.impl.TaskServiceImpl;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class TaskServiceImplIT {

  @InjectMocks TaskServiceImpl taskServiceImpl;

  @Mock TaskRepository taskRepository;
  @Mock TaskMapper taskMapper;
  @Mock CurrentUserServiceImpl currentUserServiceImpl;

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

    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTask(request)).thenReturn(mockTask);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    TaskResponse response = taskServiceImpl.create(request);

    assertNotNull(response);
    verify(taskRepository).save(mockTask);
    verify(taskMapper).toTaskResponse(mockTask);
  }

  @Test
  void getAllTasksOfCurrentUser_success() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    List<TaskResponse> result = taskServiceImpl.getAllTasksOfCurrentUser();
    assertEquals(1, result.size());
  }

  @Test
  void getTaskById_success() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    TaskResponse result = taskServiceImpl.getTaskById("task1");
    assertNotNull(result);
  }

  @Test
  void getTaskById_notFound_throwsException() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    mockUser.getTasks().clear();

    ApiException ex =
        assertThrows(ApiException.class, () -> taskServiceImpl.getTaskById("invalid"));
    assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void updateTask_success() {
    TaskUpdateRequest request =
        TaskUpdateRequest.builder().title("Updated").status(TaskStatus.COMPLETED).build();

    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    TaskResponse result = taskServiceImpl.updateTask("task1", request);

    assertEquals("Updated", mockTask.getTitle());
    assertEquals(TaskStatus.COMPLETED, mockTask.getStatus());
    verify(taskRepository).save(mockTask);
  }

  @Test
  void updateTask_notFound_throwsException() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    mockUser.getTasks().clear();

    ApiException ex =
        assertThrows(
            ApiException.class,
            () -> taskServiceImpl.updateTask("invalid", new TaskUpdateRequest()));
    assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void deleteTask_success() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);

    taskServiceImpl.deleteTask("task1");

    assertFalse(mockUser.getTasks().contains(mockTask));
    verify(taskRepository).delete(mockTask);
  }

  @Test
  void deleteTask_notFound_throwsException() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    mockUser.getTasks().clear();

    ApiException ex = assertThrows(ApiException.class, () -> taskServiceImpl.deleteTask("invalid"));
    assertEquals(ErrorCode.TASK_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void getTasksByStatus_success() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    List<TaskResponse> result = taskServiceImpl.getTasksByStatus(TaskStatus.PENDING);
    assertEquals(1, result.size());
  }

  @Test
  void searchTasks_success() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);
    when(taskMapper.toTaskResponse(mockTask)).thenReturn(taskResponse);

    List<TaskResponse> result = taskServiceImpl.searchTasks("task");
    assertEquals(1, result.size());
  }

  @Test
  void searchTasks_noMatch_returnsEmpty() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);

    List<TaskResponse> result = taskServiceImpl.searchTasks("unmatch");
    assertEquals(0, result.size());
  }

  @Test
  void getStatistics_success() {
    when(currentUserServiceImpl.getCurrentUser()).thenReturn(mockUser);

    TaskStatisticsResponse result = taskServiceImpl.getStatistics();
    assertEquals(1, result.getTotal());
    assertEquals(1, result.getPending());
    assertEquals(0, result.getCompleted());
  }
}
