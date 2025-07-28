package com.sontaypham.todolist.Controller;

import com.nimbusds.jose.JOSEException;
import com.sontaypham.todolist.DTO.Request.*;
import com.sontaypham.todolist.DTO.Response.*;
import com.sontaypham.todolist.Service.AuthenticationService;
import java.text.ParseException;
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

  @PostMapping("/introspect")
  public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
    return ApiResponse.<IntrospectResponse>builder()
        .status(1)
        .message("Introspect Method")
        .data(authenticationService.introspect(request))
        .build();
  }

  @GetMapping("/refreshToken")
  public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request)
      throws JOSEException, ParseException {
    return ApiResponse.<RefreshTokenResponse>builder()
        .status(1)
        .message("Refresh Token Method")
        .data(authenticationService.refreshToken(request))
        .build();
  }

  @PostMapping("/logout")
  ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {
    authenticationService.logout(request);
    return ApiResponse.<Void>builder().build();
  }

  @PostMapping("/reset-password")
  public ApiResponse<ResetPasswordResponse> resetPasswordEmail(
      @RequestBody ResetPasswordRequest request) {
    return ApiResponse.<ResetPasswordResponse>builder()
        .status(1)
        .data(authenticationService.resetPasswordEmail(request))
        .message("Reset Password Method")
        .build();
  }
}
