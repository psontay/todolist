package com.sontaypham.todolist.Controller.task;

import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.Controller.TaskController;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.TaskStatisticsResponse;
import com.sontaypham.todolist.Service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/statistics")
@RequiredArgsConstructor
public class TaskStatisticsController extends BaseController {

    private final TaskService taskService;

    @Operation(summary = "Get task statistics of the current user")
    @GetMapping
    public ResponseEntity<ApiResponse<TaskStatisticsResponse>> getStatistics() {
        return buildSuccessResponse("Fetched task statistics", taskService.getStatistics());
    }
}
