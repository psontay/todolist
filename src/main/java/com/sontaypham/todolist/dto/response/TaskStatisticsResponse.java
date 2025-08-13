package com.sontaypham.todolist.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStatisticsResponse {
  long total;
  long pending;
  long completed;
}
