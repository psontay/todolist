package com.sontaypham.todolist.DTO.Request;

import com.sontaypham.todolist.Validator.EmailConstraint;
import com.sontaypham.todolist.Validator.NameConstraint;
import com.sontaypham.todolist.Validator.PasswordConstraint;
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
  @NameConstraint(message = "INVALID_USERNAME")
  String name;

  @PasswordConstraint(message = "PASSWORD_TYPE_INVALID")
  String password;

  @NotBlank(message = "EMAIL_EMPTY")
  @EmailConstraint(message = "INVALID_EMAIL")
  String email;
}
