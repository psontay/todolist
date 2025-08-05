package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Request.UserUpdateRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(
    name = "User Controller",
    description = "Manage users: create, update, delete, search, assign roles")
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "Create a new user",
      description = "Register a new user with basic information")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<UserResponse>> create(
      @RequestBody UserCreationRequest request) {
    UserResponse createdUser = userService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<UserResponse>builder()
                .status(1)
                .message("User created successfully")
                .data(createdUser)
                .build());
  }

  @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
    return ResponseEntity.ok(
        ApiResponse.<List<UserResponse>>builder()
            .status(1)
            .message("All users fetched")
            .data(userService.getAllUsers())
            .build());
  }

  @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
  @GetMapping("/getUserById/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .status(1)
            .message("User found")
            .data(userService.getUserById(id))
            .build());
  }

  @Operation(summary = "Get user by email", description = "Retrieve user details by email address")
  @GetMapping("/getUserByEmail/{email}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .status(1)
            .message("User found")
            .data(userService.getUserByEmail(email))
            .build());
  }

  @Operation(summary = "Update user", description = "Update user information by ID")
  @PutMapping("/updateUser/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(
      @PathVariable String id, @RequestBody UserUpdateRequest request) {
    userService.updateUser(id, request);
    return ResponseEntity.ok(
        ApiResponse.<Void>builder().status(1).message("User updated successfully").build());
  }

  @Operation(summary = "Delete user", description = "Delete a user by ID")
  @DeleteMapping("/deleteUser/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(ApiResponse.<Void>builder().status(1).message("User deleted successfully").build());
  }

  @Operation(
      summary = "Assign role to user",
      description = "Assign a role to a user by ID and role name")
  @PostMapping("/assign-role/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> assignRoleToUser(
      @PathVariable String id, @RequestParam String roleName) {
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .status(1)
            .message("Role assigned successfully")
            .data(userService.assignRoleToUser(id, roleName))
            .build());
  }

  @Operation(
      summary = "Change password",
      description = "Update user's password by providing old and new password")
  @PutMapping("/change-password/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> updateUserPassword(
      @PathVariable String id, @RequestParam String oldPassword, @RequestParam String newPassword) {
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .status(1)
            .message("Password changed successfully")
            .data(userService.updateUserPassword(id, oldPassword, newPassword))
            .build());
  }

  @Operation(summary = "Search users", description = "Search users by keyword (name, email, etc.)")
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(@RequestParam String keyword) {
    return ResponseEntity.ok(
        ApiResponse.<List<UserResponse>>builder()
            .status(1)
            .message("Search results")
            .data(userService.searchUsers(keyword))
            .build());
  }

  @Operation(
      summary = "Get current user profile",
      description = "Get profile of the authenticated user")
  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
    return ResponseEntity.ok(
        ApiResponse.<UserResponse>builder()
            .status(1)
            .message("User profile")
            .data(userService.getUserProfile())
            .build());
  }
}
