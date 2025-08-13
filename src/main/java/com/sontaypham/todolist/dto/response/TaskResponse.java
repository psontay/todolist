package com.sontaypham.todolist.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {
  String id;
  String title;
  String status;
  String createdAt;
  String deadline;
}
