package com.sontaypham.todolist.Controller.user;

import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.Controller.UserController;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountController extends BaseController {

    private final UserService userService;

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserPassword(
            @PathVariable String id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        return buildSuccessResponse("Password changed successfully",
                              userService.updateUserPassword(id, oldPassword, newPassword));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
        return buildSuccessResponse("User profile", userService.getUserProfile());
    }
}
