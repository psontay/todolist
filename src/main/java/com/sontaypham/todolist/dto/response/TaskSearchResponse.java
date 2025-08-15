package com.sontaypham.todolist.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSearchResponse {
  private List<TaskResponse> results;
}
