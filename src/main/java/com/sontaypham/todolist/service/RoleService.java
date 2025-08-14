package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.request.RoleRequest;
import com.sontaypham.todolist.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest roleRequest);

    List<RoleResponse> getAll();

    RoleResponse findByName(String roleName);

    boolean existsByName(String roleName);

    RoleResponse findByDescription(String description);

    void deleteByName(String roleName);

    List<RoleResponse> findAllByNameIgnoreCase(String keyWord);

    RoleResponse updateFromRequest(RoleRequest roleRequest);

    RoleResponse addPermissionsToRole(String roleName, List<String> permissionNames);
}
