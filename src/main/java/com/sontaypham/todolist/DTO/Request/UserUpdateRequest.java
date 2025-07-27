package com.sontaypham.todolist.DTO.Request;

import java.util.Set;

import com.sontaypham.todolist.Validator.EmailConstraint;
import com.sontaypham.todolist.Validator.NameConstraint;
import com.sontaypham.todolist.Validator.PasswordConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  @NameConstraint(message = "INVALID_USERNAME")
  String name;
  @NotBlank(message = "EMAIL_EMPTY")
  @EmailConstraint( message = "INVALID_EMAIL")
  String email;
  @PasswordConstraint ( message = "PASSWORD_TYPE_INVALID")
  String password;
  Set<String> roles;
  Set<TaskUpdateRequest> tasks;
}
