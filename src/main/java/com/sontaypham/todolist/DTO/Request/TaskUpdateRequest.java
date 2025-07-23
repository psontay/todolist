package com.sontaypham.todolist.DTO.Request;

import com.sontaypham.todolist.Enums.TaskStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskUpdateRequest {
  String id;
  String title;
  TaskStatus status;
}
