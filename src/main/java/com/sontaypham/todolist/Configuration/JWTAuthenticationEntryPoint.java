package com.sontaypham.todolist.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.Exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
@Slf4j
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
  HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    log.error("UNAUTHENTICATED: {}", authException.getMessage());
    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ApiResponse<?> apiResponse =
        ApiResponse.builder().status(errorCode.getCode()).message(errorCode.getMessage()).build();
    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    response.flushBuffer();
  }
}
