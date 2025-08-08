package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.RoleRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Role Controller")
public class RoleController extends BaseController {

  RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @Operation(summary = "Create a new role")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest roleRequest) {
    return buildSuccessCreatedResponse("Create Role Success", roleService.create(roleRequest));
  }

  @Operation(summary = "Get list of all roles")
  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
    return buildSuccessResponse("Get All Roles Success", roleService.getAll());
  }

  @Operation(summary = "Find role by name")
  @GetMapping("/findByName/{roleName}")
  public ResponseEntity<ApiResponse<RoleResponse>> findByName(@PathVariable String roleName) {
    return buildSuccessResponse("Find Role By Name Success", roleService.findByName(roleName));
  }

  @Operation(summary = "Check if role exists by name")
  @GetMapping("/existsByName/{name}")
  public ResponseEntity<ApiResponse<Boolean>> existByName(@PathVariable String name) {
    return buildSuccessResponse("Exist Role By Name Success", roleService.existsByName(name));
  }

  @Operation(summary = "Find role by description")
  @GetMapping("/findByDescription/{des}")
  public ResponseEntity<ApiResponse<RoleResponse>> findByDescription(@PathVariable String des) {
    return buildSuccessResponse(
        "Find Role By Description Success", roleService.findByDescription(des));
  }

  @Operation(summary = "Delete role by name")
  @DeleteMapping("/delete/{name}")
  public ResponseEntity<ApiResponse<Void>> deleteByName(@PathVariable String name) {
    roleService.deleteByName(name);
    return buildSuccessNoContentResponse("Delete role success", null);
  }

  @Operation(summary = "Search roles by keyword")
  @GetMapping("/keyword/{keyword}")
  public ResponseEntity<ApiResponse<List<RoleResponse>>> findByKeyword(
      @PathVariable String keyword) {
    return buildSuccessResponse(
        "Find Roles By Keyword Success", roleService.findAllByNameIgnoreCase(keyword));
  }

  @Operation(summary = "Update an existing role")
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<RoleResponse>> update(@RequestBody RoleRequest roleRequest) {
    return buildSuccessResponse("Update Role Success", roleService.updateFromRequest(roleRequest));
  }

  @Operation(summary = "Add permissions to a role by name")
  @PutMapping("/addPermission/{name}")
  public ResponseEntity<ApiResponse<RoleResponse>> addPermission(
      @PathVariable String name, @RequestBody List<String> permissionsName) {
    return buildSuccessResponse(
        "Add Permission To Role Success", roleService.addPermissionsToRole(name, permissionsName));
  }
}
