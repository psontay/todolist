package com.sontaypham.todolist.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetails {
    String to;
    String subject;
    String messageBody;
}
