package com.sontaypham.todolist.DTO.Response;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  String id;
  String name;
  String email;
  Set<String> roles;
  Set<TaskResponse> tasks;
}
