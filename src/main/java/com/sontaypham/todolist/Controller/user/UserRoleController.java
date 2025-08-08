package com.sontaypham.todolist.Controller.user;

import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{id}/roles")
@RequiredArgsConstructor
public class UserRoleController extends BaseController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> assignRoleToUser(@PathVariable String id, @RequestParam String roleName) {
        return buildSuccessResponse("Role assigned successfully", userService.assignRoleToUser(id, roleName));
    }
}
