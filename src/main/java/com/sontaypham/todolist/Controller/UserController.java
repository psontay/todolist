package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Request.UserUpdateRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
    name = "User Controller",
    description = "Manage users: create, update, delete, search, assign roles")
public class UserController extends BaseController {

  UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(
      summary = "Create a new user",
      description = "Register a new user with basic information")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<UserResponse>> create(
      @RequestBody UserCreationRequest request) {
    return buildSuccessCreatedResponse("User created successfully", userService.create(request));
  }

  @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system")
  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
    return buildSuccessResponse("All users fetched", userService.getAllUsers());
  }

  @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
  @GetMapping("/getUserById/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
    return buildSuccessResponse("User found", userService.getUserById(id));
  }

  @Operation(summary = "Get user by email", description = "Retrieve user details by email address")
  @GetMapping("/getUserByEmail/{email}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
    return buildSuccessResponse("User found", userService.getUserByEmail(email));
  }

  @Operation(summary = "Update user", description = "Update user information by ID")
  @PutMapping("/updateUser/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(
      @PathVariable String id, @RequestBody UserUpdateRequest request) {
    userService.updateUser(id, request);
    return buildSuccessResponse("User updated successfully", null);
  }

  @Operation(summary = "Delete user", description = "Delete a user by ID")
  @DeleteMapping("/deleteUser/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return buildSuccessResponse("User deleted successfully", null);
  }

  @Operation(
      summary = "Assign role to user",
      description = "Assign a role to a user by ID and role name")
  @PostMapping("/assign-role/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> assignRoleToUser(
      @PathVariable String id, @RequestParam String roleName) {
    return buildSuccessResponse(
        "Role assigned successfully", userService.assignRoleToUser(id, roleName));
  }

  @Operation(
      summary = "Change password",
      description = "Update user's password by providing old and new password")
  @PutMapping("/change-password/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> updateUserPassword(
      @PathVariable String id, @RequestParam String oldPassword, @RequestParam String newPassword) {
    return buildSuccessResponse(
        "Password changed successfully",
        userService.updateUserPassword(id, oldPassword, newPassword));
  }

  @Operation(summary = "Search users", description = "Search users by keyword (name, email, etc.)")
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(@RequestParam String keyword) {
    return buildSuccessResponse("Search results", userService.searchUsers(keyword));
  }

  @Operation(
      summary = "Get current user profile",
      description = "Get profile of the authenticated user")
  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
    return buildSuccessResponse("User profile", userService.getUserProfile());
  }
}
