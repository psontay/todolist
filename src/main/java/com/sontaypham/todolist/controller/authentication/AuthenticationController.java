package com.sontaypham.todolist.controller.authentication;

import com.nimbusds.jose.JOSEException;
import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.request.AuthenticationRequest;
import com.sontaypham.todolist.dto.request.IntrospectRequest;
import com.sontaypham.todolist.dto.request.LogoutRequest;
import com.sontaypham.todolist.dto.request.RefreshTokenRequest;
import com.sontaypham.todolist.dto.request.ResetPasswordRequest;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.AuthenticationResponse;
import com.sontaypham.todolist.dto.response.IntrospectResponse;
import com.sontaypham.todolist.dto.response.RefreshTokenResponse;
import com.sontaypham.todolist.dto.response.ResetPasswordResponse;
import com.sontaypham.todolist.service.AuthenticationService;
import com.sontaypham.todolist.service.impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
@Slf4j
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest request) {
        log.info("Received request login user");
        return buildSuccessResponse("Login Method", authenticationService.authenticate(request));
    }

    @Operation(summary = "Validate a JWT token and get introspection info")
    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(
            @RequestBody IntrospectRequest request) {
        return buildSuccessResponse("Introspect Method", authenticationService.introspect(request));
    }

    @Operation(summary = "Refresh JWT access token using a refresh token")
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request) throws JOSEException, ParseException {
        return buildSuccessResponse(
                "Refresh Token Method", authenticationService.refreshToken(request));
    }

    @Operation(summary = "Send password reset link to user's email")
    @PostMapping(value = "/reset-password",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPasswordEmail(
            @RequestBody ResetPasswordRequest request) {
        return buildSuccessResponse(
                "Reset Password Method", authenticationService.resetPasswordEmail(request));
    }

    @Operation(summary = "Logout user and invalidate tokens")
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return buildSuccessNoContentResponse("Logout Method", null);
    }

}
