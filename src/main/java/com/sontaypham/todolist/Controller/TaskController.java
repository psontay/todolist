package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.TaskCreationRequest;
import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.DTO.Response.TaskStatisticsResponse;
import com.sontaypham.todolist.Enums.TaskStatus;
import com.sontaypham.todolist.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskResponse>> create(@RequestBody TaskCreationRequest request) {
        TaskResponse response = taskService.create(request);
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                                            .status(1)
                                            .message("Task created successfully")
                                            .data(response)
                                            .build());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasksOfCurrentUser();
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                                            .status(1)
                                            .message("Fetched all tasks")
                                            .data(tasks)
                                            .build());
    }

    @GetMapping("/getTaskById/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable String id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                                            .status(1)
                                            .message("Fetched task successfully")
                                            .data(task)
                                            .build());
    }

    @PutMapping("/updateTask/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable String id,
            @RequestBody TaskUpdateRequest request) {
        TaskResponse task = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                                            .status(1)
                                            .message("Task updated successfully")
                                            .data(task)
                                            .build());
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                                            .status(1)
                                            .message("Task deleted successfully")
                                            .data(null)
                                            .build());
    }

    @GetMapping("/getTasksByStatus/{status}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByStatus(
            @PathVariable TaskStatus status) {
        List<TaskResponse> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                                            .status(1)
                                            .message("Fetched tasks by status")
                                            .data(tasks)
                                            .build());
    }

    @GetMapping("/searchTasks/{keyword}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> searchTasks(
            @PathVariable String keyword) {
        List<TaskResponse> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                                            .status(1)
                                            .message("Searched tasks successfully")
                                            .data(tasks)
                                            .build());
    }

    @GetMapping("/getStatistics")
    public ResponseEntity<ApiResponse<TaskStatisticsResponse>> getStatistics() {
        TaskStatisticsResponse stats = taskService.getStatistics();
        return ResponseEntity.ok(ApiResponse.<TaskStatisticsResponse>builder()
                                            .status(1)
                                            .message("Fetched task statistics")
                                            .data(stats)
                                            .build());
    }
}
