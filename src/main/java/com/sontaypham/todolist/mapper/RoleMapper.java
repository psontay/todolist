package com.sontaypham.todolist.mapper;

import com.sontaypham.todolist.dto.request.RoleRequest;
import com.sontaypham.todolist.dto.response.RoleResponse;
import com.sontaypham.todolist.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {
  @Mapping(source = "name", target = "name")
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest roleRequest);

  @Mapping(source = "name", target = "name")
  RoleResponse toRoleResponse(Role role);

  @Mapping(target = "permissions", ignore = true)
  void updateRoleFromRequest(RoleRequest roleRequest, @MappingTarget Role role);
}
