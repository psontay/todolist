package com.sontaypham.todolist.DTO.Request;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  String name;
  String email;
  String password;
  Set<String> roles;
  Set<TaskUpdateRequest> tasks;
}
