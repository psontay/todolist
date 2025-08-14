package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.request.PermissionRequest;
import com.sontaypham.todolist.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {
  PermissionResponse createPermission(PermissionRequest request);

  List<PermissionResponse> getPermissions();

  PermissionResponse findByName(String permissionName);

  boolean existsByName(String permissionName);

  PermissionResponse findByDescription(String description);

  PermissionResponse updatePermissionByName(String permissionName, PermissionRequest request);

  void deletePermissionByName(String permissionName);

  List<PermissionResponse> searchByKeyword(String keyword);
}
