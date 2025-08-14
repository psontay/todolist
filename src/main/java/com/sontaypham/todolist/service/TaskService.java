package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.request.TaskCreationRequest;
import com.sontaypham.todolist.dto.request.TaskUpdateRequest;
import com.sontaypham.todolist.dto.response.TaskResponse;
import com.sontaypham.todolist.dto.response.TaskStatisticsResponse;
import com.sontaypham.todolist.enums.TaskStatus;
import java.util.List;

public interface TaskService {
  TaskResponse create(TaskCreationRequest request);

  List<TaskResponse> getAllTasksOfCurrentUser();

  TaskResponse getTaskById(String id);

  TaskResponse updateTask(String id, TaskUpdateRequest request);

  void deleteTask(String id);

  List<TaskResponse> getTasksByStatus(TaskStatus status);

  List<TaskResponse> searchTasks(String keyword);

  TaskStatisticsResponse getStatistics();

  String getCurrentUserId();
}
