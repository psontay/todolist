package com.sontaypham.todolist.controller.user;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.request.UserCreationRequest;
import com.sontaypham.todolist.dto.request.UserUpdateRequest;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCrudController extends BaseController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> create(
      @Valid @RequestBody UserCreationRequest request) {
    return buildSuccessCreatedResponse("User created successfully", userService.create(request));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
      @RequestParam(required = false) String email) {
    if (email != null) {
      return buildSuccessResponse("User found", List.of(userService.getUserByEmail(email)));
    }
    return buildSuccessResponse("All users fetched", userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
    return buildSuccessResponse("User found", userService.getUserById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(
      @PathVariable String id, @Valid @RequestBody UserUpdateRequest request) {
    userService.updateUser(id, request);
    return buildSuccessNoContentResponse("User updated successfully", null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return buildSuccessNoContentResponse("User deleted successfully", null);
  }
}
