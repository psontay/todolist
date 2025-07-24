package com.sontaypham.todolist.Mapper;

import com.sontaypham.todolist.DTO.Request.PermissionRequest;
import com.sontaypham.todolist.DTO.Response.PermissionResponse;
import com.sontaypham.todolist.Entities.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission( PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    void updatePermission( PermissionRequest permissionRequest, @MappingTarget Permission permission);
}
