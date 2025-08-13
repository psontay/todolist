package com.sontaypham.todolist.controller.task;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.request.TaskCreationRequest;
import com.sontaypham.todolist.dto.request.TaskUpdateRequest;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.TaskResponse;
import com.sontaypham.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskCrudController extends BaseController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> create(@Valid @RequestBody TaskCreationRequest request) {
        return buildSuccessCreatedResponse("Task created successfully", taskService.create(request));
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
        return buildSuccessResponse("Fetched all tasks", taskService.getAllTasksOfCurrentUser());
    }

    @Operation(summary = "Get task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable String id) {
        return buildSuccessResponse("Fetched task successfully", taskService.getTaskById(id));
    }

    @Operation(summary = "Update task by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable String id,
            @Valid @RequestBody TaskUpdateRequest request) {
        return buildSuccessResponse("Task updated successfully", taskService.updateTask(id, request));
    }

    @Operation(summary = "Delete task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return buildSuccessResponse("Task deleted successfully", null);
    }
}
