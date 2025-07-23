package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Request.UserUpdateRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/create")
  public ApiResponse<UserResponse> create(@RequestBody UserCreationRequest request) {
    return ApiResponse.<UserResponse>builder()
                      .status(1)
                      .message("Create User Success!")
                      .data(userService.create(request))
                      .build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<UserResponse>> getAllUsers() {
    return ApiResponse.<List<UserResponse>>builder()
                      .status(1)
                      .message("All Users")
                      .data(userService.getAllUsers())
                      .build();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
    return ApiResponse.<UserResponse>builder()
                      .status(1)
                      .message("User Found")
                      .data(userService.getUserById(id))
                      .build();
  }

  @GetMapping("/email")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<UserResponse> getUserByEmail(@RequestParam String email) {
    return ApiResponse.<UserResponse>builder()
                      .status(1)
                      .message("User Found")
                      .data(userService.getUserByEmail(email))
                      .build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<Void> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {
    userService.updateUser(id, request);
    return ApiResponse.<Void>builder()
                      .status(1)
                      .message("User Updated Successfully")
                      .build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
  public ApiResponse<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ApiResponse.<Void>builder()
                      .status(1)
                      .message("User Deleted Successfully")
                      .build();
  }

  @PostMapping("/{id}/assign-role")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<UserResponse> assignRoleToUser(@PathVariable String id, @RequestParam String roleName) {
    return ApiResponse.<UserResponse>builder()
                      .status(1)
                      .message("Role Assigned Successfully")
                      .data(userService.assignRoleToUser(id, roleName))
                      .build();
  }

  @PutMapping("/{id}/change-password")
  public ApiResponse<UserResponse> updateUserPassword(
          @PathVariable String id,
          @RequestParam String oldPassword,
          @RequestParam String newPassword) {
    return ApiResponse.<UserResponse>builder()
                      .status(1)
                      .message("Password Changed Successfully")
                      .data(userService.updateUserPassword(id, oldPassword, newPassword))
                      .build();
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<List<UserResponse>> searchUsers(@RequestParam String keyword) {
    return ApiResponse.<List<UserResponse>>builder()
                      .status(1)
                      .message("Search Result")
                      .data(userService.searchUsers(keyword))
                      .build();
  }

  @GetMapping("/profile")
  public ApiResponse<UserResponse> getUserProfile() {
    return ApiResponse.<UserResponse>builder()
                      .status(1)
                      .message("User Profile")
                      .data(userService.getUserProfile())
                      .build();
  }
}
