package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.RoleRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Service.RoleService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoleController {

  RoleService roleService;

  @PostMapping("/create")
  public ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
    return ApiResponse.<RoleResponse>builder()
        .status(1)
        .message("Create Role Success")
        .data(roleService.create(roleRequest))
        .build();
  }

  @GetMapping("/list")
  public ApiResponse<List<RoleResponse>> getAll() {
    return ApiResponse.<List<RoleResponse>>builder()
        .status(1)
        .message("Get All Roles Success")
        .data(roleService.getAll())
        .build();
  }

  @PostMapping("/findByName/{roleName}")
  public ApiResponse<RoleResponse> findByName(@PathVariable String roleName) {
    return ApiResponse.<RoleResponse>builder()
        .status(1)
        .message("Find Role By Name Success")
        .data(roleService.findByName(roleName))
        .build();
  }

  @GetMapping("/existsByName/{name}")
  public ApiResponse<Boolean> existByName(@PathVariable String name) {
    return ApiResponse.<Boolean>builder()
        .status(1)
        .message("Exist Role By Name Success")
        .data(roleService.existsByName(name))
        .build();
  }

  @GetMapping("/findByDescription/{des}")
  public ApiResponse<RoleResponse> findByDescription(@PathVariable String des) {
    return ApiResponse.<RoleResponse>builder()
        .status(1)
        .message("Find Role By Description Success")
        .data(roleService.findByDescription(des))
        .build();
  }

  @Transactional
  @DeleteMapping("/delete/{name}")
  public ApiResponse<String> deleteByName(@PathVariable String name) {
    roleService.deleteByName(name);
    return ApiResponse.<String>builder()
        .status(1)
        .message("Delete Role Success")
        .data("Role deleted successfully")
        .build();
  }

  @GetMapping("/keyword/{keyword}")
  public ApiResponse<List<RoleResponse>> findByKeyword(@PathVariable String keyword) {
    return ApiResponse.<List<RoleResponse>>builder()
        .status(1)
        .message("Find Roles By Keyword Success")
        .data(roleService.findAllByNameIgnoreCase(keyword))
        .build();
  }

  @Transactional
  @PutMapping("/update")
  public ApiResponse<RoleResponse> update(@RequestBody RoleRequest roleRequest) {
    return ApiResponse.<RoleResponse>builder()
        .status(1)
        .message("Update Role Success")
        .data(roleService.updateFromRequest(roleRequest))
        .build();
  }

  @Transactional
  @PutMapping("/addPermission/{name}")
  public ApiResponse<RoleResponse> addPermission(
      @PathVariable String name, @RequestBody List<String> permissionsName) {
    return ApiResponse.<RoleResponse>builder()
        .status(1)
        .message("Add Permission To Role Success")
        .data(roleService.addPermissionsToRole(name, permissionsName))
        .build();
  }
}
