package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.TaskCreationRequest;
import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Response.TaskResponse;
import com.sontaypham.todolist.Entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  Task toTask(TaskCreationRequest request);

  Task toTask(TaskUpdateRequest request);

  @Mapping(target = "status", source = "status", qualifiedByName = "enumToString")
  @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "dateToString")
  @Mapping(target = "deadline" , source = "deadline" , qualifiedByName = "dateToString")
  TaskResponse toTaskResponse(Task task);

  @Named("enumToString")
  default String enumToString(Enum<?> e) {
    return e != null ? e.name() : null;
  }

  @Named("dateToString")
  default String dateToString(java.time.LocalDateTime dt) {
    return dt != null ? dt.toString() : null;
  }
}
