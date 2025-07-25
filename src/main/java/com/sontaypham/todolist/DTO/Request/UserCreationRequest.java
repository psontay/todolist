package com.sontaypham.todolist.DTO.Request;

import com.sontaypham.todolist.Validator.EmailConstraint;
import com.sontaypham.todolist.Validator.PasswordConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.sontaypham.todolist.Validator.NameConstraint;
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
  @Size(min = 3, message = "USERNAME_LENGTH_INVALID")
  @NameConstraint(message = "INVALID_USERNAME")
  String name;

  @Size(min = 6, message = "PASSWORD_INVALID")
  @PasswordConstraint(message = "PASSWORD_TYPE_INVALID")
  String password;

  @NotBlank(message = "EMAIL_EMPTY")
  @EmailConstraint(domain = "user@", message = "INVALID_EMAIL")
  String email;
}
