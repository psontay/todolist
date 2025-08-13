package com.sontaypham.todolist.mapper;

import com.sontaypham.todolist.dto.request.PermissionRequest;
import com.sontaypham.todolist.dto.response.PermissionResponse;
import com.sontaypham.todolist.entities.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest permissionRequest);

  PermissionResponse toPermissionResponse(Permission permission);

  void updatePermission(PermissionRequest permissionRequest, @MappingTarget Permission permission);
}
