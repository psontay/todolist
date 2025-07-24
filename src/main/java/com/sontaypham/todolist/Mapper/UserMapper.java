package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Entities.Role;
import com.sontaypham.todolist.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest request);

  @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
  @Mapping(target = "tasks", source = "tasks")
  UserResponse toUserResponse(User user);
  @Named("rolesToStrings")
  static Set<String> rolesToStrings(Set<Role> roles) {
    return roles == null ? null : roles.stream().map(Role::getName).collect(Collectors.toSet());
  }
}
