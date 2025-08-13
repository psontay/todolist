package com.sontaypham.todolist.integration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sontaypham.todolist.dto.request.RoleRequest;
import com.sontaypham.todolist.dto.response.RoleResponse;
import com.sontaypham.todolist.entities.Permission;
import com.sontaypham.todolist.entities.Role;
import com.sontaypham.todolist.exception.ApiException;
import com.sontaypham.todolist.exception.ErrorCode;
import com.sontaypham.todolist.mapper.RoleMapper;
import com.sontaypham.todolist.repository.PermissionRepository;
import com.sontaypham.todolist.repository.RoleRepository;
import com.sontaypham.todolist.service.RoleService;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class RoleServiceIT {

  @InjectMocks RoleService roleService;
  @Mock RoleRepository roleRepository;
  @Mock PermissionRepository permissionRepository;
  @Mock RoleMapper roleMapper;

  Role role;
  RoleRequest roleRequest;
  RoleResponse roleResponse;
  Permission permission;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    permission = Permission.builder().name("CREATE_TASK").description("desc").build();

    role =
        Role.builder()
            .name("ADMIN")
            .description("Administrator")
            .permissions(new HashSet<>(Set.of(permission)))
            .build();

    roleRequest =
        RoleRequest.builder()
            .name("ADMIN")
            .description("Administrator")
            .permissions(Set.of("CREATE_TASK"))
            .build();

    roleResponse =
        RoleResponse.builder()
            .name("ADMIN")
            .description("Administrator")
            .permissions(Set.of()) // or mock PermissionResponse if needed
            .build();
  }

  @Test
  void createRole_success() {
    when(roleRepository.existsByName("ADMIN")).thenReturn(false);
    when(permissionRepository.findAllByNameIn(anySet())).thenReturn(List.of(permission));
    when(roleMapper.toRole(roleRequest)).thenReturn(role);
    when(roleRepository.save(role)).thenReturn(role);
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    RoleResponse result = roleService.create(roleRequest);

    assertEquals("ADMIN", result.getName());
    verify(roleRepository).save(role);
  }

  @Test
  void createRole_alreadyExists_throwsException() {
    when(roleRepository.existsByName("ADMIN")).thenReturn(true);

    ApiException ex = assertThrows(ApiException.class, () -> roleService.create(roleRequest));
    assertEquals(ErrorCode.ROLE_ALREADY_EXISTS, ex.getErrorCode());
  }

  @Test
  void createRole_permissionNotFound_throwsException() {
    when(roleRepository.existsByName("ADMIN")).thenReturn(false);
    when(permissionRepository.findAllByNameIn(anySet())).thenReturn(Collections.emptyList());

    ApiException ex = assertThrows(ApiException.class, () -> roleService.create(roleRequest));
    assertEquals(ErrorCode.PERMISSION_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void getAll_success() {
    when(roleRepository.findAll()).thenReturn(List.of(role));
    when(roleMapper.toRoleResponse(any())).thenReturn(roleResponse);

    List<RoleResponse> result = roleService.getAll();

    assertEquals(1, result.size());
    verify(roleRepository).findAll();
  }

  @Test
  void findByName_success() {
    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    RoleResponse result = roleService.findByName("ADMIN");

    assertEquals("ADMIN", result.getName());
  }

  @Test
  void findByName_notFound_throwsException() {
    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

    ApiException ex = assertThrows(ApiException.class, () -> roleService.findByName("ADMIN"));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void existsByName_true() {
    when(roleRepository.existsByName("ADMIN")).thenReturn(true);
    assertTrue(roleService.existsByName("ADMIN"));
  }

  @Test
  void findByDescription_success() {
    when(roleRepository.findByDescription("Administrator")).thenReturn(Optional.of(role));
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    RoleResponse result = roleService.findByDescription("Administrator");

    assertEquals("ADMIN", result.getName());
  }

  @Test
  void findByDescription_notFound_throwsException() {
    when(roleRepository.findByDescription("Administrator")).thenReturn(Optional.empty());

    ApiException ex =
        assertThrows(ApiException.class, () -> roleService.findByDescription("Administrator"));

    assertEquals(ErrorCode.ROLE_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void deleteByName_success() {
    when(roleRepository.existsByName("ADMIN")).thenReturn(true);
    doNothing().when(roleRepository).deleteByName("ADMIN");

    assertDoesNotThrow(() -> roleService.deleteByName("ADMIN"));
  }

  @Test
  void deleteByName_notFound_throwsException() {
    when(roleRepository.existsByName("ADMIN")).thenReturn(false);

    ApiException ex = assertThrows(ApiException.class, () -> roleService.deleteByName("ADMIN"));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void findAllByNameIgnoreCase_success() {
    when(roleRepository.findAllByNameContainingIgnoreCase("adm")).thenReturn(List.of(role));
    when(roleMapper.toRoleResponse(any())).thenReturn(roleResponse);

    List<RoleResponse> results = roleService.findAllByNameIgnoreCase("adm");

    assertEquals(1, results.size());
  }

  @Test
  void updateFromRequest_success() {
    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
    doNothing().when(roleMapper).updateRoleFromRequest(roleRequest, role);
    when(roleRepository.save(role)).thenReturn(role);
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    RoleResponse result = roleService.updateFromRequest(roleRequest);
    assertEquals("ADMIN", result.getName());
  }

  @Test
  void updateFromRequest_notFound_throwsException() {
    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

    ApiException ex =
        assertThrows(ApiException.class, () -> roleService.updateFromRequest(roleRequest));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void addPermissionsToRole_success() {
    List<String> permissionNames = List.of("CREATE_TASK");

    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
    when(permissionRepository.findAllByNameIn(new HashSet<>(permissionNames)))
        .thenReturn(List.of(permission));
    when(roleRepository.save(role)).thenReturn(role);
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    RoleResponse result = roleService.addPermissionsToRole("ADMIN", permissionNames);
    assertEquals("ADMIN", result.getName());
  }

  @Test
  void addPermissionsToRole_missingPermissions_throwsException() {
    List<String> permissionNames = List.of("CREATE_TASK", "DELETE_TASK");
    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
    when(permissionRepository.findAllByNameIn(anySet())).thenReturn(List.of(permission));

    ApiException ex =
        assertThrows(
            ApiException.class, () -> roleService.addPermissionsToRole("ADMIN", permissionNames));
    assertEquals(ErrorCode.PERMISSION_NOT_FOUND, ex.getErrorCode());
  }

  @Test
  void addPermissionsToRole_roleNotFound_throwsException() {
    when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

    ApiException ex =
        assertThrows(
            ApiException.class,
            () -> roleService.addPermissionsToRole("ADMIN", List.of("CREATE_TASK")));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, ex.getErrorCode());
  }
}
