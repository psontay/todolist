package com.sontaypham.todolist.integration.service;

import com.sontaypham.todolist.DTO.Request.PermissionRequest;
import com.sontaypham.todolist.DTO.Response.PermissionResponse;
import com.sontaypham.todolist.Entities.Permission;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Mapper.PermissionMapper;
import com.sontaypham.todolist.Repository.PermissionRepository;
import com.sontaypham.todolist.Service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

    @Mock
    PermissionRepository permissionRepository;

    @Mock
    PermissionMapper permissionMapper;

    @InjectMocks
    PermissionService permissionService;

    Permission permission;
    PermissionRequest request;
    PermissionResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        permission = Permission.builder().name("TASK_VIEW").description("View tasks").build();
        request = PermissionRequest.builder().name("TASK_VIEW").description("View tasks").build();
        response = PermissionResponse.builder().name("TASK_VIEW").description("View tasks").build();
    }

    @Test
    void createPermission_success() {
        when(permissionRepository.existsByName("TASK_VIEW")).thenReturn(false);
        when(permissionMapper.toPermission(request)).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

        PermissionResponse result = permissionService.createPermission(request);

        assertEquals("TASK_VIEW", result.getName());
        verify(permissionRepository).save(permission);
    }

    @Test
    void createPermission_alreadyExists_throwsException() {
        when(permissionRepository.existsByName("TASK_VIEW")).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            permissionService.createPermission(request);
        });

        assertEquals(ErrorCode.PERMISSION_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void getPermissions_success() {
        List<Permission> permissions = List.of(permission);
        when(permissionRepository.findAll()).thenReturn(permissions);
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

        List<PermissionResponse> results = permissionService.getPermissions();

        assertEquals(1, results.size());
        assertEquals("TASK_VIEW", results.getFirst().getName());
    }

    @Test
    void findByName_success() {
        when(permissionRepository.findByName("TASK_VIEW")).thenReturn(Optional.of(permission));
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

        PermissionResponse result = permissionService.findByName("TASK_VIEW");

        assertNotNull(result);
        assertEquals("TASK_VIEW", result.getName());
    }

    @Test
    void findByName_notFound_throwsException() {
        when(permissionRepository.findByName("TASK_VIEW")).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                                                      permissionService.findByName("TASK_VIEW")
                                             );

        assertEquals(ErrorCode.PERMISSION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updatePermissionByName_success() {
        PermissionRequest updateRequest = PermissionRequest.builder()
                                                           .name("TASK_VIEW").description("Updated").build();

        when(permissionRepository.findByName("TASK_VIEW")).thenReturn(Optional.of(permission));
        doAnswer(inv -> {
            permission.setDescription("Updated");
            return null;
        }).when(permissionMapper).updatePermission(updateRequest, permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(
                new PermissionResponse("TASK_VIEW", "Updated")
                                                                          );

        PermissionResponse result = permissionService.updatePermissionByName("TASK_VIEW", updateRequest);

        assertEquals("Updated", result.getDescription());
    }

    @Test
    void updatePermissionByName_notFound_throwsException() {
        when(permissionRepository.findByName("UNKNOWN")).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                                                      permissionService.updatePermissionByName("UNKNOWN", request)
                                             );

        assertEquals(ErrorCode.PERMISSION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deletePermissionByName_success() {
        when(permissionRepository.existsByName("TASK_VIEW")).thenReturn(true);

        permissionService.deletePermissionByName("TASK_VIEW");

        verify(permissionRepository).deletePermissionByName("TASK_VIEW");
    }

    @Test
    void deletePermissionByName_notFound_throwsException() {
        when(permissionRepository.existsByName("TASK_VIEW")).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () ->
                                                      permissionService.deletePermissionByName("TASK_VIEW")
                                             );

        assertEquals(ErrorCode.PERMISSION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void searchByKeyword_success() {
        when(permissionRepository.findAllByNameContainingIgnoreCase("TASK"))
                .thenReturn(List.of(permission));
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

        List<PermissionResponse> results = permissionService.searchByKeyword("TASK");

        assertEquals(1, results.size());
        assertEquals("TASK_VIEW", results.getFirst().getName());
    }
}
