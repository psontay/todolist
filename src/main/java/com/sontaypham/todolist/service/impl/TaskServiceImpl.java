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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,
        makeFinal = true)
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
    @Cacheable(cacheNames = "task-list",
            key = "#root.target.getCurrentUserId()")
    public List<TaskResponse> getAllTasksOfCurrentUser() {
        String userId = getCurrentUserId();
        return taskRepository.findByUserId(userId)
                             .stream()
                             .map(taskMapper :: toTaskResponse)
                             .toList();
    }

    @Override
    @Cacheable(cacheNames = "task-by-id",
            key = "#id")
    public TaskResponse getTaskById(String id) {
        String userId = getCurrentUserId();
        Task task =
                taskRepository.findByIdAndUserId(id, userId)
                              .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
        return taskMapper.toTaskResponse(task);
    }

    @Override
    @CacheEvict(
            cacheNames = {"task-statistics", "task-list", "task-by-status"},
            key = "#root.target.getCurrentUserId()",
            allEntries = true)
    @CachePut(cacheNames = "task-by-id",
            key = "#id")
    public TaskResponse updateTask(String id, TaskUpdateRequest request) {
        String userId = getCurrentUserId();
        Task existingTask =
                taskRepository.findByIdAndUserId(id, userId)
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
            cacheNames = {"task-statistics", "task-list", "task-status"},
            key = "#root.target.getCurrentUserId()",
            allEntries = true)
    @Transactional
    public void deleteTask(String id) {
        String userId = getCurrentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId)
                                  .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));

        log.info("Deleting task with id {}", id);
        taskRepository.delete(task);
    }

    @Override
    @Cacheable(cacheNames = "task-by-status",
            key = "#status + '_' + #root.target.getCurrentUserId()")
    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        String userId = getCurrentUserId();
        return taskRepository.findByUserIdAndStatus(userId, status)
                             .stream()
                             .map(taskMapper :: toTaskResponse)
                             .toList();
    }

    @Override
    @Cacheable(
            cacheNames = "task-by-keyword",
            key = "#keyword + '_' + #root.target.getCurrentUserId()")
    public TaskSearchResponse searchTasks(String keyword) {
        String userId = getCurrentUserId();
        List<TaskResponse> matched = taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, keyword)
                                                   .stream()
                                                   .map(taskMapper :: toTaskResponse)
                                                   .toList();
        TaskSearchResponse response = new TaskSearchResponse();
        response.setResults(matched);
        return response;
    }
    
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
        return sortedTask.stream()
                         .map(taskMapper :: toTaskResponse)
                         .toList();
    }

    @Override
    public List<TaskResponse> sortTasksByDeadline() {
        User user = currentUserService.getCurrentUser();
        Set<Task> currentTasks = user.getTasks();
        List<Task> sortedTask =
                currentTasks.stream()
                            .sorted(
                                    (t1, t2) -> {
                                        if (t1.getDeadline() == null) {
                                            return 1;
                                        }
                                        if (t2.getDeadline() == null) {
                                            return - 1;
                                        }
                                        return t1.getDeadline()
                                                 .compareTo(t2.getDeadline());
                                    })
                            .toList();
        return sortedTask.stream()
                         .map(taskMapper :: toTaskResponse)
                         .toList();
    }

    @Override
    @Cacheable(cacheNames = "task-statistics",
            key = "#root.target.getCurrentUserId()")
    public TaskStatisticsResponse getStatistics() {
        String userId = getCurrentUserId();
        long total = taskRepository.countByUserId(userId);
        long pending = taskRepository.countByUserIdAndStatus(userId, TaskStatus.PENDING);
        long completed = taskRepository.countByUserIdAndStatus(userId, TaskStatus.COMPLETED);

        return TaskStatisticsResponse.builder()
                                     .total(total)
                                     .pending(pending)
                                     .completed(completed)
                                     .build();
    }

    @Override
    public String getCurrentUserId() {
        return currentUserService.getCurrentUser()
                                 .getId();
    }

}
