package com.sontaypham.todolist.dto.request;

import com.sontaypham.todolist.validator.EmailConstraint;
import com.sontaypham.todolist.validator.PasswordConstraint;
import com.sontaypham.todolist.validator.UsernameConstraint;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  @UsernameConstraint(message = "INVALID_USERNAME")
  String userName;

  @EmailConstraint(message = "INVALID_EMAIL")
  String email;

  @PasswordConstraint(message = "PASSWORD_TYPE_INVALID")
  String password;

  Set<String> roles;
  Set<TaskUpdateRequest> tasks;
}
