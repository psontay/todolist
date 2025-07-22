package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Response.ApiResonpe;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;
    @PostMapping("/create")
    public ApiResonpe<UserResponse> create(@RequestBody UserCreationRequest request) {
        return ApiResonpe.<UserResponse>builder().status(1).message("Create User Success!").data(userService.create(request)).build();
    }
}
