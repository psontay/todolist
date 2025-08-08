package com.sontaypham.todolist.Controller.user;

import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Request.UserUpdateRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCrudController extends BaseController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreationRequest request) {
        return buildSuccessCreatedResponse("User created successfully", userService.create(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(@RequestParam(required = false) String email) {
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
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        return buildSuccessNoContentResponse("User updated successfully", null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return buildSuccessNoContentResponse("User deleted successfully", null);
    }
}
