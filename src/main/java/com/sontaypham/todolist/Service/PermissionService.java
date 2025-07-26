package com.sontaypham.todolist.Service;

import com.sontaypham.todolist.DTO.Request.PermissionRequest;
import com.sontaypham.todolist.DTO.Response.PermissionResponse;
import com.sontaypham.todolist.Entities.Permission;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Mapper.PermissionMapper;
import com.sontaypham.todolist.Repository.PermissionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableMethodSecurity
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  @PreAuthorize("hasRole('ADMIN')")
  public PermissionResponse createPermission(PermissionRequest request) {
    log.info("Create Permission : {}", request.getName());
    if (permissionRepository.existsByName(request.getName()))
      throw new ApiException(ErrorCode.PERMISSION_ALREADY_EXISTS);
    Permission permission = permissionMapper.toPermission(request);
    permission = permissionRepository.save(permission);
    return permissionMapper.toPermissionResponse(permission);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<PermissionResponse> getPermissions() {
    log.info("method getPermissions");
    return permissionRepository.findAll().stream()
        .map(permissionMapper::toPermissionResponse)
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public PermissionResponse findByName(String permissionName) {
    log.info("method findPermissionByName : {}", permissionName);
    return permissionRepository
        .findByName(permissionName)
        .map(permissionMapper::toPermissionResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.PERMISSION_NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public boolean existsByName(String permissionName) {
    log.info("method existsPermissionByPermissionName : {}", permissionName);
    return permissionRepository.existsByName(permissionName);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public PermissionResponse findByDescription(String description) {
    log.info("method findByDescription : {}", description);
    return permissionRepository
        .findByDescription(description)
        .map(permissionMapper::toPermissionResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.PERMISSION_NOT_FOUND));
  }

  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public PermissionResponse updatePermissionByName(
      String permissionName, PermissionRequest request) {
    log.info("method updatePermissionByName : {}", permissionName);
    Permission permission =
        permissionRepository
            .findByName(permissionName)
            .orElseThrow(() -> new ApiException(ErrorCode.PERMISSION_NOT_FOUND));
    permissionMapper.updatePermission(request, permission);
    permissionRepository.save(permission);
    return permissionMapper.toPermissionResponse(permission);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void deletePermissionByName(String permissionName) {
    log.info("method deletePermissionByName : {}", permissionName);
    if (!permissionRepository.existsByName(permissionName))
      throw new ApiException(ErrorCode.PERMISSION_NOT_FOUND);
    permissionRepository.deletePermissionByName(permissionName);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<PermissionResponse> searchByKeyword(String keyword) {
    log.info("method searchByKeyword : {}", keyword);
    return permissionRepository.findAllByNameContainingIgnoreCase(keyword).stream()
        .map(permissionMapper::toPermissionResponse)
        .toList();
  }
}
