package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.RoleRequest;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Entities.Role;
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
