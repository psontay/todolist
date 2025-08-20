package com.sontaypham.todolist.dto.request;

import com.sontaypham.todolist.validator.EmailConstraint;
import com.sontaypham.todolist.validator.PasswordConstraint;
import com.sontaypham.todolist.validator.UsernameConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
  @UsernameConstraint(message = "INVALID_USERNAME")
  String userName;

  @PasswordConstraint(message = "PASSWORD_TYPE_INVALID")
  String password;

  @NotBlank(message = "EMAIL_EMPTY")
  @EmailConstraint(message = "INVALID_EMAIL")
  String email;
}
