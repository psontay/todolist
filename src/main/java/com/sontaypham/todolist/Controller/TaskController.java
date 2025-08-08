package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.TaskCreationRequest;
import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Response.buildSuccessResponse;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.DTO.Response.TaskStatisticsResponse;
import com.sontaypham.todolist.Enums.TaskStatus;
import com.sontaypham.todolist.Service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController extends BaseController {

  TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @Operation(summary = "Create a new task")
  @PostMapping("/create")
  public ResponseEntity<buildSuccessResponse<TaskResponse>> create(
      @RequestBody TaskCreationRequest request) {
    return buildSuccessCreatedResponse("Create Task Success", taskService.create(request));
  }

  @Operation(summary = "Get all tasks of the current user")
  @GetMapping("/list")
  public ResponseEntity<buildSuccessResponse<List<TaskResponse>>> getAllTasks() {
    List<TaskResponse> tasks = taskService.getAllTasksOfCurrentUser();
    return buildSuccessResponse("Fetched all tasks", tasks);
  }

  @Operation(summary = "Get task by ID")
  @GetMapping("/getTaskById/{id}")
  public ResponseEntity<buildSuccessResponse<TaskResponse>> getTaskById(@PathVariable String id) {
    TaskResponse task = taskService.getTaskById(id);
    return buildSuccessResponse("Fetched task successfully", task);
  }

  @Operation(summary = "Update task by ID")
  @PutMapping("/updateTask/{id}")
  public ResponseEntity<buildSuccessResponse<TaskResponse>> updateTask(
      @PathVariable String id, @RequestBody TaskUpdateRequest request) {
    TaskResponse task = taskService.updateTask(id, request);
    return buildSuccessResponse("Task updated successfully", task);
  }

  @Operation(summary = "Delete task by ID")
  @DeleteMapping("/deleteTask/{id}")
  public ResponseEntity<buildSuccessResponse<Void>> deleteTask(@PathVariable String id) {
    taskService.deleteTask(id);
    return buildSuccessResponse("Task deleted successfully", null);
  }

  @Operation(summary = "Get tasks by status")
  @GetMapping("/getTasksByStatus/{status}")
  public ResponseEntity<buildSuccessResponse<List<TaskResponse>>> getTasksByStatus(
      @PathVariable TaskStatus status) {
    List<TaskResponse> tasks = taskService.getTasksByStatus(status);
    return buildSuccessResponse("Fetched tasks by status", tasks);
  }

  @Operation(summary = "Search tasks by keyword")
  @GetMapping("/searchTasks/{keyword}")
  public ResponseEntity<buildSuccessResponse<List<TaskResponse>>> searchTasks(@PathVariable String keyword) {
    List<TaskResponse> tasks = taskService.searchTasks(keyword);
    return buildSuccessResponse("Searched tasks successfully", tasks);
  }

  @Operation(summary = "Get task statistics of the current user")
  @GetMapping("/getStatistics")
  public ResponseEntity<buildSuccessResponse<TaskStatisticsResponse>> getStatistics() {
    TaskStatisticsResponse stats = taskService.getStatistics();
    return buildSuccessResponse("Fetched task statistics", stats);
  }
}
