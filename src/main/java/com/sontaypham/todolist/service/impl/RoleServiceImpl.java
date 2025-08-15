package com.sontaypham.todolist.service.impl;

import com.sontaypham.todolist.dto.request.RoleRequest;
import com.sontaypham.todolist.dto.response.RoleResponse;
import com.sontaypham.todolist.entities.Permission;
import com.sontaypham.todolist.entities.Role;
import com.sontaypham.todolist.exception.*;
import com.sontaypham.todolist.mapper.*;
import com.sontaypham.todolist.repository.*;
import com.sontaypham.todolist.service.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
  RoleRepository roleRepository;
  RoleMapper roleMapper;
  PermissionRepository permissionRepository;

  @Override
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "role-list", key = "'all'"),
        @CacheEvict(cacheNames = "role-keyword", allEntries = true)
      })
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

  @Override
  @Cacheable(cacheNames = "role-list", key = "'all'")
  @PreAuthorize("hasRole('ADMIN')")
  public List<RoleResponse> getAll() {
    return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
  }

  @Override
  @Cacheable(cacheNames = "role-by-name", key = "#roleName")
  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse findByName(String roleName) {
    log.info("<Find Role Method> {}", roleName);
    return roleRepository
        .findByName(roleName)
        .map(roleMapper::toRoleResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public boolean existsByName(String roleName) {
    log.info("<Exists Role Method> {}", roleName);
    return roleRepository.existsByName(roleName);
  }

  @Override
  @Cacheable(cacheNames = "role-by-des", key = "#des")
  @PreAuthorize("hasRole('ADMIN')")
  public RoleResponse findByDescription(String des) {
    log.info("<Find Role By Description Method> {}", des);
    return roleRepository
        .findByDescription(des)
        .map(roleMapper::toRoleResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "role-by-name", key = "#roleName"),
        @CacheEvict(cacheNames = "role-list", key = "'all'"),
        @CacheEvict(cacheNames = "role-keyword", allEntries = true)
      })
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteByName(String roleName) {
    log.info("<Delete Role Method> {}", roleName);
    if (!roleRepository.existsByName(roleName)) throw new ApiException(ErrorCode.ROLE_NOT_FOUND);
    roleRepository.deleteByName(roleName);
  }

  @Override
  @Cacheable(cacheNames = "role-keyword", key = "#keyWord")
  @PreAuthorize("hasRole('ADMIN')")
  public List<RoleResponse> findAllByNameIgnoreCase(String keyWord) {
    log.info("<Find Role By Keyword Method> {}", keyWord);
    return roleRepository.findAllByNameContainingIgnoreCase(keyWord).stream()
        .map(roleMapper::toRoleResponse)
        .toList();
  }

  @Override
  @CacheEvict(
      cacheNames = {"role-list", "role-keyword"},
      allEntries = true)
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

  @Override
  @CacheEvict(
      cacheNames = {"role-list", "role-keyword"},
      allEntries = true)
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
      List<String> foundNames = permissions.stream().map(Permission::getName).toList();
      List<String> missing = permissionNames.stream().filter(p -> !foundNames.contains(p)).toList();
      throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND, "Missing Permission : " + missing);
    }
    if (role.getPermissions() == null) role.setPermissions(new HashSet<>());
    System.out.println("Before : " + role.getPermissions().size());
    role.getPermissions().addAll(permissions);
    System.out.println("After : " + role.getPermissions().size());
    Role save = roleRepository.save(role);
    return roleMapper.toRoleResponse(save);
  }
}
