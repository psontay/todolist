package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.RoleRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Role Controller")
public class RoleController {

  RoleService roleService;

  @Operation(summary = "Create a new role")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest roleRequest) {
    RoleResponse response = roleService.create(roleRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<RoleResponse>builder()
                .status(1)
                .message("Create Role Success")
                .data(response)
                .build());
  }

  @Operation(summary = "Get list of all roles")
  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
    List<RoleResponse> roles = roleService.getAll();
    return ResponseEntity.ok(
        ApiResponse.<List<RoleResponse>>builder()
            .status(1)
            .message("Get All Roles Success")
            .data(roles)
            .build());
  }

  @Operation(summary = "Find role by name")
  @GetMapping("/findByName/{roleName}")
  public ResponseEntity<ApiResponse<RoleResponse>> findByName(@PathVariable String roleName) {
    RoleResponse response = roleService.findByName(roleName);
    return ResponseEntity.ok(
        ApiResponse.<RoleResponse>builder()
            .status(1)
            .message("Find Role By Name Success")
            .data(response)
            .build());
  }

  @Operation(summary = "Check if role exists by name")
  @GetMapping("/existsByName/{name}")
  public ResponseEntity<ApiResponse<Boolean>> existByName(@PathVariable String name) {
    boolean exists = roleService.existsByName(name);
    return ResponseEntity.ok(
        ApiResponse.<Boolean>builder()
            .status(1)
            .message("Exist Role By Name Success")
            .data(exists)
            .build());
  }

  @Operation(summary = "Find role by description")
  @GetMapping("/findByDescription/{des}")
  public ResponseEntity<ApiResponse<RoleResponse>> findByDescription(@PathVariable String des) {
    RoleResponse response = roleService.findByDescription(des);
    return ResponseEntity.ok(
        ApiResponse.<RoleResponse>builder()
            .status(1)
            .message("Find Role By Description Success")
            .data(response)
            .build());
  }

  @Operation(summary = "Delete role by name")
  @DeleteMapping("/delete/{name}")
  public ResponseEntity<ApiResponse<Void>> deleteByName(@PathVariable String name) {
    roleService.deleteByName(name);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(
            ApiResponse.<Void>builder()
                .status(1)
                .message("Delete Role Success")
                .data(null)
                .build());
  }

  @Operation(summary = "Search roles by keyword")
  @GetMapping("/keyword/{keyword}")
  public ResponseEntity<ApiResponse<List<RoleResponse>>> findByKeyword(
      @PathVariable String keyword) {
    List<RoleResponse> roles = roleService.findAllByNameIgnoreCase(keyword);
    return ResponseEntity.ok(
        ApiResponse.<List<RoleResponse>>builder()
            .status(1)
            .message("Find Roles By Keyword Success")
            .data(roles)
            .build());
  }

  @Operation(summary = "Update an existing role")
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<RoleResponse>> update(@RequestBody RoleRequest roleRequest) {
    RoleResponse response = roleService.updateFromRequest(roleRequest);
    return ResponseEntity.ok(
        ApiResponse.<RoleResponse>builder()
            .status(1)
            .message("Update Role Success")
            .data(response)
            .build());
  }

  @Operation(summary = "Add permissions to a role by name")
  @PutMapping("/addPermission/{name}")
  public ResponseEntity<ApiResponse<RoleResponse>> addPermission(
      @PathVariable String name, @RequestBody List<String> permissionsName) {
    RoleResponse response = roleService.addPermissionsToRole(name, permissionsName);
    return ResponseEntity.ok(
        ApiResponse.<RoleResponse>builder()
            .status(1)
            .message("Add Permission To Role Success")
            .data(response)
            .build());
  }
}
