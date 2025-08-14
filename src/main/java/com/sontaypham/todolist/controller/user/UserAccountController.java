package com.sontaypham.todolist.controller.user;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.service.UserService;
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
      @PathVariable String id, @RequestParam String oldPassword, @RequestParam String newPassword) {
    return buildSuccessResponse(
        "Password changed successfully",
        userService.updateUserPassword(id, oldPassword, newPassword));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
    return buildSuccessResponse("User profile", userService.getUserProfile());
  }
}
