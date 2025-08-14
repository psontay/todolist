package com.sontaypham.todolist.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sontaypham.todolist.dto.request.*;
import com.sontaypham.todolist.dto.response.AuthenticationResponse;
import com.sontaypham.todolist.dto.response.IntrospectResponse;
import com.sontaypham.todolist.dto.response.RefreshTokenResponse;
import com.sontaypham.todolist.dto.response.ResetPasswordResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request);
    SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
    void assertTokenNotRevoked(SignedJWT jwt) throws ParseException;
    ResetPasswordResponse resetPasswordEmail(ResetPasswordRequest request);
}
