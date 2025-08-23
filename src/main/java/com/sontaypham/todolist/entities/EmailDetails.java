package com.sontaypham.todolist.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetails {
  String to;
  String subject;
  String messageBody;
  String templateName;
  List<String> attachmentPaths;
  Map<String, Object> variables;
}
