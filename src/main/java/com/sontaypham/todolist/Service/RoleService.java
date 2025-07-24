package com.sontaypham.todolist.Service;

import com.sontaypham.todolist.DTO.Request.RoleRequest;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Entities.Permission;
import com.sontaypham.todolist.Entities.Role;
import com.sontaypham.todolist.Exception.*;
import com.sontaypham.todolist.Mapper.*;
import com.sontaypham.todolist.Repository.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  RoleMapper roleMapper;
  PermissionRepository permissionRepository;

  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse create(RoleRequest roleRequest) {
    log.info("<Create Role Method> {}", roleRequest.getName());
    if (roleRepository.existsByName(roleRequest.getName())) {
      throw new ApiException(ErrorCode.ROLE_ALREADY_EXISTS);
    }
    Set<String> permissionNames = roleRequest.getPermissions();
    List<Permission> permissions = permissionRepository.findAllByNameIn(permissionNames);
    if (permissions.isEmpty()) {
      throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND);
    }
    Role role = roleMapper.toRole(roleRequest);
    role.setPermissions(new HashSet<>(permissions));
    role = roleRepository.save(role);
    return roleMapper.toRoleResponse(role);
  }

  public List<RoleResponse> getAll() {
    return roleRepository.findAll().stream()
        .map(roleMapper::toRoleResponse)
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse findByName(String roleName) {
    log.info("<Find Role Method> {}", roleName);
    return roleRepository
        .findByName(roleName)
        .map(roleMapper::toRoleResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public boolean existsByName(String roleName) {
    log.info("<Exists Role Method> {}", roleName);
    return roleRepository.existsByName(roleName);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse findByDescription(String des) {
    log.info("<Find Role By Description Method> {}", des);
    return roleRepository
        .findByDescription(des)
        .map(roleMapper::toRoleResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void deleteByName(String roleName) {
    log.info("<Delete Role Method> {}", roleName);
    if (!roleRepository.existsByName(roleName)) throw new ApiException(ErrorCode.ROLE_NOT_FOUND);
    roleRepository.deleteByName(roleName);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<RoleResponse> findAllByNameIgnoreCase(String keyWord) {
    log.info("<Find Role By Keyword Method> {}", keyWord);
    return roleRepository.findAllByNameContainingIgnoreCase(keyWord).stream()
        .map(roleMapper::toRoleResponse)
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse updateFromRequest(RoleRequest roleRequest) {
    log.info("<Update Role Method> {}", roleRequest.getName());
    Role role =
        roleRepository
            .findByName(roleRequest.getName())
            .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    roleMapper.updateRoleFromRequest(roleRequest, role);
    roleRepository.save(role);
    return roleMapper.toRoleResponse(role);
  }

  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse addPermissionsToRole(String roleName, List<String> permissionNames) {
    Role role =
        roleRepository
            .findByName(roleName)
            .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    Set<String> names = new HashSet<>(permissionNames);
    List<Permission> permissions = permissionRepository.findAllByNameIn(names);
    if (permissions.size() != permissionNames.size()) {
      List<String> foundNames =
          permissions.stream().map(Permission::getName).collect(Collectors.toList());
      List<String> missing = permissionNames.stream().filter(p -> !foundNames.contains(p)).toList();
      throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND, "Missing Permission : " + missing);
    }
    role.getPermissions().addAll(permissions);
    Role save = roleRepository.save(role);
    return roleMapper.toRoleResponse(save);
  }
}
