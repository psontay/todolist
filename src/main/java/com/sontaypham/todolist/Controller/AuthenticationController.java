package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.AuthenticationRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.AuthenticationResponse;
import com.sontaypham.todolist.Service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationController {
  @Autowired private AuthenticationService authenticationService;

  @PostMapping("/login")
  public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    return ApiResponse.<AuthenticationResponse>builder()
        .status(1)
        .message("Login Method")
        .data(authenticationService.authenticate(request))
        .build();
  }
}
