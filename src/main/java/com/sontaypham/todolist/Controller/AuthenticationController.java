package com.sontaypham.todolist.Controller;

import com.nimbusds.jose.JOSEException;
import com.sontaypham.todolist.DTO.Request.*;
import com.sontaypham.todolist.DTO.Response.*;
import com.sontaypham.todolist.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import java.text.ParseException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@NoArgsConstructor
@AllArgsConstructor
@Tag( name = "Authentication")
public class AuthenticationController {

  @Autowired private AuthenticationService authenticationService;

  @Operation(summary = "Authenticate user and return JWT token")
  @PostMapping("/login")
  public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    return ApiResponse.<AuthenticationResponse>builder()
                      .status(1)
                      .message("Login Method")
                      .data(authenticationService.authenticate(request))
                      .build();
  }

  @Operation(summary = "Validate a JWT token and get introspection info")
  @PostMapping("/introspect")
  public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
    return ApiResponse.<IntrospectResponse>builder()
                      .status(1)
                      .message("Introspect Method")
                      .data(authenticationService.introspect(request))
                      .build();
  }

  @Operation(summary = "Refresh JWT access token using a refresh token")
  @GetMapping("/refreshToken")
  public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request)
          throws JOSEException, ParseException {
    return ApiResponse.<RefreshTokenResponse>builder()
                      .status(1)
                      .message("Refresh Token Method")
                      .data(authenticationService.refreshToken(request))
                      .build();
  }

  @Operation(summary = "Logout user and invalidate tokens")
  @PostMapping("/logout")
  ApiResponse<Void> logout(@RequestBody LogoutRequest request)
          throws ParseException, JOSEException {
    authenticationService.logout(request);
    return ApiResponse.<Void>builder().build();
  }

  @Operation(summary = "Send password reset link to user's email")
  @PostMapping(
          value = "/reset-password",
          produces = MediaType.APPLICATION_JSON_VALUE
  )  public ApiResponse<ResetPasswordResponse> resetPasswordEmail(
          @RequestBody ResetPasswordRequest request) {
    return ApiResponse.<ResetPasswordResponse>builder()
                      .status(1)
                      .data(authenticationService.resetPasswordEmail(request))
                      .message("Reset Password Method")
                      .build();
  }
}
