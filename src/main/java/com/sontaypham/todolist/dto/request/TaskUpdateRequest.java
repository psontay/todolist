package com.sontaypham.todolist.dto.request;

import com.sontaypham.todolist.enums.TaskStatus;
import java.time.LocalDateTime;
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
  LocalDateTime deadline;
}
