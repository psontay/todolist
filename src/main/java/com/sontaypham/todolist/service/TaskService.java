package com.sontaypham.todolist.service;

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
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
  TaskRepository taskRepository;
  TaskMapper taskMapper;
  CurrentUserService currentUserService;

  @CachePut(cacheNames = "task-create", key = "#result.id")
  @CacheEvict(
      cacheNames = {"task-list", "task-statistics"},
      key = "#root.target.getCurrentUserId()")
  public TaskResponse create(TaskCreationRequest request) {
    User user = currentUserService.getCurrentUser();
    Task task = taskMapper.toTask(request);
    task.setUser(user);
    task.setStatus(TaskStatus.PENDING);
    task.setDeadline(request.getDeadline());
    task.setWarningEmailSent(false);
    taskRepository.save(task);
    return taskMapper.toTaskResponse(task);
  }

  @Cacheable(cacheNames = "task-list", key = "#root.target.getCurrentUserId()")
  public List<TaskResponse> getAllTasksOfCurrentUser() {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    return currentTasks.stream().map(taskMapper::toTaskResponse).toList();
  }

  @Cacheable(cacheNames = "task-by-id", key = "#id")
  public TaskResponse getTaskById(String id) {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    Task isExistsTask =
        currentTasks.stream()
            .filter(task -> task.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
    return taskMapper.toTaskResponse(isExistsTask);
  }

  @CacheEvict(
      cacheNames = {"task-statistics", "task-list"},
      key = "#root.target.getCurrentUserId()")
  @CachePut(cacheNames = "task-by-id", key = "#id")
  public TaskResponse updateTask(String id, TaskUpdateRequest request) {
    User user = currentUserService.getCurrentUser();
    Task existingTask =
        user.getTasks().stream()
            .filter(obj -> obj.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
    existingTask.setTitle(request.getTitle());
    existingTask.setStatus(request.getStatus());
    existingTask.setDeadline(request.getDeadline());
    existingTask.setWarningEmailSent(false);
    taskRepository.save(existingTask);
    return taskMapper.toTaskResponse(existingTask);
  }

  @CacheEvict(
      cacheNames = {"task-statistics", "task-list"},
      key = "#root.target.getCurrentUserId()")
  @Transactional
  public void deleteTask(String id) {
    User user = currentUserService.getCurrentUser();
    Set<Task> tasks = user.getTasks();

    Task task =
        user.getTasks().stream()
            .filter(obj -> obj.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));

    tasks.remove(task);
    log.info("Deleting task with id {}", id);
    taskRepository.delete(task);
  }

  @Cacheable(cacheNames = "task-by-status", key = "#status + '_' + #root.target.getCurrentUserId()")
  public List<TaskResponse> getTasksByStatus(TaskStatus status) {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    return currentTasks.stream()
        .filter(obj -> obj.getStatus().equals(status))
        .map(taskMapper::toTaskResponse)
        .toList();
  }

  @Cacheable(
      cacheNames = "task-by-keyword",
      key = "#keyword + '_' + #root.target.getCurrentUserId()")
  public List<TaskResponse> searchTasks(String keyword) {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    return currentTasks.stream()
        .filter(obj -> obj.getTitle().toLowerCase().contains(keyword.toLowerCase()))
        .map(taskMapper::toTaskResponse)
        .toList();
  }

  @Cacheable(cacheNames = "task-statistics", key = "#root.target.getCurrentUserId()")
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

  public String getCurrentUserId() {
    return currentUserService.getCurrentUser().getId();
  }
}
