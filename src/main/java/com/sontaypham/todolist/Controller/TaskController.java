package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.TaskCreationRequest;
import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.DTO.Response.TaskStatisticsResponse;
import com.sontaypham.todolist.Enums.TaskStatus;
import com.sontaypham.todolist.Service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag( name = "Task Controller")
public class TaskController {

  private final TaskService taskService;

  @Operation(summary = "Create a new task")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<TaskResponse>> create(
          @RequestBody TaskCreationRequest request) {
    TaskResponse response = taskService.create(request);
    return ResponseEntity.ok(
            ApiResponse.<TaskResponse>builder()
                       .status(1)
                       .message("Task created successfully")
                       .data(response)
                       .build());
  }

  @Operation(summary = "Get all tasks of the current user")
  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
    List<TaskResponse> tasks = taskService.getAllTasksOfCurrentUser();
    return ResponseEntity.ok(
            ApiResponse.<List<TaskResponse>>builder()
                       .status(1)
                       .message("Fetched all tasks")
                       .data(tasks)
                       .build());
  }

  @Operation(summary = "Get task by ID")
  @GetMapping("/getTaskById/{id}")
  public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable String id) {
    TaskResponse task = taskService.getTaskById(id);
    return ResponseEntity.ok(
            ApiResponse.<TaskResponse>builder()
                       .status(1)
                       .message("Fetched task successfully")
                       .data(task)
                       .build());
  }

  @Operation(summary = "Update task by ID")
  @PutMapping("/updateTask/{id}")
  public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
          @PathVariable String id, @RequestBody TaskUpdateRequest request) {
    TaskResponse task = taskService.updateTask(id, request);
    return ResponseEntity.ok(
            ApiResponse.<TaskResponse>builder()
                       .status(1)
                       .message("Task updated successfully")
                       .data(task)
                       .build());
  }

  @Operation(summary = "Delete task by ID")
  @DeleteMapping("/deleteTask/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
    taskService.deleteTask(id);
    return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                       .status(1)
                       .message("Task deleted successfully")
                       .data(null)
                       .build());
  }

  @Operation(summary = "Get tasks by status")
  @GetMapping("/getTasksByStatus/{status}")
  public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByStatus(
          @PathVariable TaskStatus status) {
    List<TaskResponse> tasks = taskService.getTasksByStatus(status);
    return ResponseEntity.ok(
            ApiResponse.<List<TaskResponse>>builder()
                       .status(1)
                       .message("Fetched tasks by status")
                       .data(tasks)
                       .build());
  }

  @Operation(summary = "Search tasks by keyword")
  @GetMapping("/searchTasks/{keyword}")
  public ResponseEntity<ApiResponse<List<TaskResponse>>> searchTasks(@PathVariable String keyword) {
    List<TaskResponse> tasks = taskService.searchTasks(keyword);
    return ResponseEntity.ok(
            ApiResponse.<List<TaskResponse>>builder()
                       .status(1)
                       .message("Searched tasks successfully")
                       .data(tasks)
                       .build());
  }

  @Operation(summary = "Get task statistics of the current user")
  @GetMapping("/getStatistics")
  public ResponseEntity<ApiResponse<TaskStatisticsResponse>> getStatistics() {
    TaskStatisticsResponse stats = taskService.getStatistics();
    return ResponseEntity.ok(
            ApiResponse.<TaskStatisticsResponse>builder()
                       .status(1)
                       .message("Fetched task statistics")
                       .data(stats)
                       .build());
  }
}
