package com.sontaypham.todolist.mapper;

import com.sontaypham.todolist.dto.request.UserCreationRequest;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.entities.Role;
import com.sontaypham.todolist.entities.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest request);

  @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
  @Mapping(target = "tasks", source = "tasks")
  @Mapping(target = "username" , source = "username")
  UserResponse toUserResponse(User user);

  @Named("rolesToStrings")
  static Set<String> rolesToStrings(Set<Role> roles) {
    return roles == null ? null : roles.stream().map(Role::getName).collect(Collectors.toSet());
  }
}
