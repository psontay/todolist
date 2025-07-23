package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired UserService userService;

  @PostMapping("/create")
  public ApiResponse<UserResponse> create(@RequestBody UserCreationRequest request) {
    return ApiResponse.<UserResponse>builder()
        .status(1)
        .message("Create User Success!")
        .data(userService.create(request))
        .build();
  }
}
