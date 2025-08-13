package com.sontaypham.todolist.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

  // USER
  USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
  USERNAME_ALREADY_EXISTS(1002, "Username already exists", HttpStatus.CONFLICT),
  EMAIL_ALREADY_EXISTS(1003, "Email already exists", HttpStatus.CONFLICT),
  INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
  INVALID_CREDENTIALS(1005, "Invalid username or password", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(1006, "Unauthorized", HttpStatus.UNAUTHORIZED),
  FORBIDDEN(1007, "Forbidden", HttpStatus.FORBIDDEN),
  UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  INVALID_USERNAME(1009, "Invalid username", HttpStatus.BAD_REQUEST),
  INVALID_EMAIL(10010, "Invalid email type , must end with @gmail.com", HttpStatus.BAD_REQUEST),
  PASSWORD_NOT_MATCHES(10011, "Password not matches!", HttpStatus.BAD_REQUEST),
  PASSWORD_TYPE_INVALID(
      10012,
      "Password type invalid , length must be at least {min} characters && at least 1 upper case!",
      HttpStatus.BAD_REQUEST),

  // TASK
  TASK_NOT_FOUND(2001, "Task not found", HttpStatus.NOT_FOUND),
  TASK_ACCESS_DENIED(2002, "You do not have permission to access this task", HttpStatus.FORBIDDEN),
  TASK_ALREADY_EXISTS(2003, "Task already exists", HttpStatus.CONFLICT),

  //  SERVER & VALIDATION
  VALIDATION_ERROR(3001, "Validation failed", HttpStatus.BAD_REQUEST),
  BAD_REQUEST(3002, "Bad request", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR(3003, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
  RESOURCE_CONFLICT(3004, "Resource conflict", HttpStatus.CONFLICT),
  INVALID_INPUT(3005, "Invalid input", HttpStatus.BAD_REQUEST),
  METHOD_NOT_ALLOWED(3006, "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),

  // TOKEN & AUTH
  TOKEN_EXPIRED(4001, "Token has expired", HttpStatus.UNAUTHORIZED),
  TOKEN_INVALID(4002, "Invalid token", HttpStatus.UNAUTHORIZED),
  TOKEN_MISSING(4003, "Missing authentication token", HttpStatus.UNAUTHORIZED),
  UNCATEGORIZED(9999, "Uncategorized", HttpStatus.CONFLICT),
  // ROLE & PERMISSION
  ROLE_NOT_FOUND(5001, "Role not found", HttpStatus.NOT_FOUND),
  ROLE_ALREADY_EXISTS(5002, "Role already exists", HttpStatus.CONFLICT),
  PERMISSION_NOT_FOUND(5003, "Permission not found", HttpStatus.NOT_FOUND),
  PERMISSION_ALREADY_EXISTS(5004, "Permission already exists", HttpStatus.CONFLICT),
  ROLE_ASSIGNMENT_FAILED(5005, "Failed to assign role to user", HttpStatus.INTERNAL_SERVER_ERROR),
  PERMISSION_ASSIGN_FAILED(
      5006, "Failed to assign permission to role", HttpStatus.INTERNAL_SERVER_ERROR),
  ROLE_DELETE_FORBIDDEN(5007, "Cannot delete this role", HttpStatus.FORBIDDEN),
  PERMISSION_DELETE_FORBIDDEN(5008, "Cannot delete this permission", HttpStatus.FORBIDDEN);

  int code;
  String message;
  HttpStatus httpStatus;
}
