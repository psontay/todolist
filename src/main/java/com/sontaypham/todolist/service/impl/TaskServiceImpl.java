package com.sontaypham.todolist.service.impl;

import com.sontaypham.todolist.dto.request.TaskCreationRequest;
import com.sontaypham.todolist.dto.request.TaskUpdateRequest;
import com.sontaypham.todolist.dto.response.TaskResponse;
import com.sontaypham.todolist.dto.response.TaskSearchResponse;
import com.sontaypham.todolist.dto.response.TaskStatisticsResponse;
import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.TaskStatus;
import com.sontaypham.todolist.exception.ApiException;
import com.sontaypham.todolist.exception.ErrorCode;
import com.sontaypham.todolist.mapper.TaskMapper;
import com.sontaypham.todolist.repository.TaskRepository;
import com.sontaypham.todolist.service.CurrentUserService;
import com.sontaypham.todolist.service.TaskService;
import java.util.Comparator;
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
public class TaskServiceImpl implements TaskService {
  TaskRepository taskRepository;
  TaskMapper taskMapper;
  CurrentUserService currentUserService;

  @Override
  @CacheEvict(
      cacheNames = {"task-list", "task-statistics", "task-by-status"},
      key = "#root.target.getCurrentUserId()",
      allEntries = true)
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

  @Override
  @CacheEvict( cacheNames = "task-statistics" , key = "#root.target.getCurrentUserId()" ,  allEntries = true)
  @Cacheable(cacheNames = "task-list", key = "#root.target.getCurrentUserId()")
  public List<TaskResponse> getAllTasksOfCurrentUser() {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    return currentTasks.stream().map(taskMapper::toTaskResponse).toList();
  }

  @Override
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

  @Override
  @CacheEvict(
      cacheNames = {"task-statistics", "task-list" , "task-by-status"},
      key = "#root.target.getCurrentUserId()",
      allEntries = true)
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

  @Override
  @CacheEvict(
      cacheNames = {"task-statistics", "task-list" , "task-status"},
      key = "#root.target.getCurrentUserId()",
      allEntries = true)
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

  @Override
  @Cacheable(cacheNames = "task-by-status", key = "#status + '_' + #root.target.getCurrentUserId()")
  public List<TaskResponse> getTasksByStatus(TaskStatus status) {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    return currentTasks.stream()
        .filter(obj -> obj.getStatus().equals(status))
        .map(taskMapper::toTaskResponse)
        .toList();
  }

  @Override
  @Cacheable(
      cacheNames = "task-by-keyword",
      key = "#keyword + '_' + #root.target.getCurrentUserId()")
  public TaskSearchResponse searchTasks(String keyword) {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    List<TaskResponse> matched =
        currentTasks.stream()
            .filter(obj -> obj.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .map(taskMapper::toTaskResponse)
            .toList();
    TaskSearchResponse response = new TaskSearchResponse();
    response.setResults(matched);
    return response;
  }

  @Override
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

  @CacheEvict(cacheNames = "task-by-status", key = "#root.target.getCurrentUserId()")
  @Override
  public List<TaskResponse> sortTasksByStatus() {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    List<Task> sortedTask =
        currentTasks.stream()
            .sorted(
                Comparator.comparingInt(
                    task -> {
                      switch (task.getStatus()) {
                        case PENDING:
                          return 0;
                        case IN_PROGRESS:
                          return 1;
                        case COMPLETED:
                          return 2;
                        default:
                          return 3;
                      }
                    }))
            .toList();
    return sortedTask.stream().map(taskMapper::toTaskResponse).toList();
  }

  @Override
  public List<TaskResponse> sortTasksByDeadline() {
    User user = currentUserService.getCurrentUser();
    Set<Task> currentTasks = user.getTasks();
    List<Task> sortedTask =
        currentTasks.stream()
            .sorted(
                (t1, t2) -> {
                  if (t1.getDeadline() == null) return 1;
                  if (t2.getDeadline() == null) return -1;
                  return t1.getDeadline().compareTo(t2.getDeadline());
                })
            .toList();
    return sortedTask.stream().map(taskMapper::toTaskResponse).toList();
  }

  @Override
  public String getCurrentUserId() {
    return currentUserService.getCurrentUser().getId();
  }
}
