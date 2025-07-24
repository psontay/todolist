package com.sontaypham.todolist.DTO.Response;

import com.sontaypham.todolist.Entities.Task;
import com.sontaypham.todolist.Enums.TaskStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
}
