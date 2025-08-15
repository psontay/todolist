package com.sontaypham.todolist.controller.task;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.TaskResponse;
import com.sontaypham.todolist.dto.response.TaskSearchResponse;
import com.sontaypham.todolist.enums.TaskStatus;
import com.sontaypham.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskQueryController extends BaseController {

  private final TaskService taskService;

  @Operation(summary = "Get tasks by status")
  @GetMapping(params = "status")
  public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByStatus(
      @RequestParam TaskStatus status) {
    return buildSuccessResponse("Fetched tasks by status", taskService.getTasksByStatus(status));
  }

  @Operation(summary = "Sort tasks by status")
  @GetMapping("/sort/status")
  public ResponseEntity<ApiResponse<List<TaskResponse>>> sortByStatus() {
    List<TaskResponse> result = taskService.sortTasksByStatus();
    return buildSuccessResponse("Sorted tasks by status", result);
  }
  @Operation( summary = "Sort task by deadline")
  @GetMapping("/sort/deadline")
  public ResponseEntity<ApiResponse<List<TaskResponse>>> sortByDeadline() {
      List<TaskResponse> result = taskService.sortTasksByDeadline();
      return buildSuccessResponse("Sorted tasks by deadline", result);
  }
  @Operation(summary = "Search tasks by keyword")
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<TaskSearchResponse>> searchTasks(@RequestParam String keyword) {
    return buildSuccessResponse("Searched tasks successfully", taskService.searchTasks(keyword));
  }
}
