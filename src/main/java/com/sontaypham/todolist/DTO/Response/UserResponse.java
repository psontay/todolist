package com.sontaypham.todolist.DTO.Response;

import com.sontaypham.todolist.Entities.Role;
import com.sontaypham.todolist.Entities.Task;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String name;
    String email;
    Set<Role> roles;
    Set<Task> tasks;
}
