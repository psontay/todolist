package com.sontaypham.todolist.dto.request;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskCreationRequest {
  String title;
  LocalDateTime deadline;
}
