package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
  protected <T> ResponseEntity<ApiResponse<T>> buildSuccessResponse(String message, T data) {
    return ResponseEntity.ok(
            ApiResponse.<T>builder().status(1).message(message).data(data).build());
  }

  protected <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(String message, T data) {
    return ResponseEntity.status(500)
        .body(ApiResponse.<T>builder().status(0).message(message).data(data).build());
  }

  protected <T> ResponseEntity<ApiResponse<T>> buildSuccessNoContentResponse(
      String message, T data) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(ApiResponse.<T>builder().status(1).message(message).data(data).build());
  }

  protected <T> ResponseEntity<ApiResponse<T>> buildSuccessCreatedResponse(String message, T data) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.<T>builder().status(1).message(message).data(data).build());
  }
}
