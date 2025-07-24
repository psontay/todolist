package com.sontaypham.todolist.Service;

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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
    TaskRepository taskRepository;
    TaskMapper taskMapper;
    CurrentUserService currentUserService;

    public TaskResponse create (TaskCreationRequest request) {
        Task task = taskMapper.toTask(request);
        User user = currentUserService.getCurrentUser();
        task.setUser(user);
        task.setStatus(TaskStatus.PENDING);
        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }
    public List<TaskResponse> getAllTasksOfCurrentUser() {
        User user = currentUserService.getCurrentUser();
        Set<Task> currentTasks = user.getTasks();
        return currentTasks.stream().map(taskMapper::toTaskResponse).toList();
    }
    public TaskResponse getTaskById(String id) {
        User user = currentUserService.getCurrentUser();
        Set<Task> currentTasks = user.getTasks();
        Task isExistsTask = currentTasks.stream()
                                        .filter(task -> task.getId().equals(id))
                                        .findFirst().orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
        return taskMapper.toTaskResponse(isExistsTask);
    }
    public TaskResponse updateTask(String id, TaskUpdateRequest request) {
        User user = currentUserService.getCurrentUser();
        Task existingTask =
                user.getTasks().stream().filter( obj -> obj.getId().equals(id)).findFirst().orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
        existingTask.setTitle(request.getTitle());
        existingTask.setStatus(request.getStatus());
        taskRepository.save(existingTask);
        return taskMapper.toTaskResponse(existingTask);
    }
    public void deleteTask(String id) {
        User user = currentUserService.getCurrentUser();
        Set<Task> currentTasks = user.getTasks();
        Task task = currentTasks.stream().filter( obj -> obj.getId().equals(id)).findFirst().orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
        log.info("Deleting task with id {}", id);
        taskRepository.delete(task);
    }
    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        User user = currentUserService.getCurrentUser();
        Set<Task> currentTasks = user.getTasks();
        return currentTasks.stream().filter(obj -> obj.getStatus().equals(status)).map(taskMapper::toTaskResponse).toList();
    }
    public List<TaskResponse> searchTasks(String keyword) {
        User user = currentUserService.getCurrentUser();
        Set<Task> currentTasks = user.getTasks();
        return currentTasks.stream().filter(obj -> obj.getTitle().toLowerCase().contains(keyword.toLowerCase())).map(taskMapper::toTaskResponse).toList();
    }
    public TaskStatisticsResponse getStatistics() {
        User user = currentUserService.getCurrentUser();
        Set<Task> tasks = user.getTasks();
        long total = tasks.size();
        long pending = tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).count();
        long completed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();

        return TaskStatisticsResponse.builder()
                                     .total(total)
                                     .pending(pending)
                                     .completed(completed)
                                     .build();
    }
}
