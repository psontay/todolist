package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest request);

  @Mapping(target = "roles", source = "roles")
  @Mapping(target = "tasks", source = "tasks")
  @Mapping(target = "id", source = "id")
  UserResponse toUserResponse(User user);
}
