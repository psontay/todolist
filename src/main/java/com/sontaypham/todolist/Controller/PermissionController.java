package com.sontaypham.todolist.Controller;

import com.sontaypham.todolist.DTO.Request.PermissionRequest;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.PermissionResponse;
import com.sontaypham.todolist.Service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission Controller")
public class PermissionController extends BaseController {

  PermissionService permissionService;

  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @Operation(summary = "Create a new permission")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
      @RequestBody @Valid PermissionRequest permissionRequest) {
    return buildSuccessCreatedResponse(
        "<Create Permission Successfully>", permissionService.createPermission(permissionRequest));
  }

  @Operation(summary = "Get list of all permissions")
  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<PermissionResponse>>> listPermission() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("<Username>" + authentication.getName());
    authentication
        .getAuthorities()
        .forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
    return buildSuccessResponse(
        "<List Permission Successfully>", permissionService.getPermissions());
  }

  @Operation(summary = "Find permission by name")
  @GetMapping("/findByName/{name}")
  public ResponseEntity<ApiResponse<PermissionResponse>> findPermissionByName(
      @PathVariable String name) {
    return buildSuccessResponse(
        "<Find Permission Successfully>", permissionService.findByName(name));
  }

  @Operation(summary = "Check if a permission exists by name")
  @GetMapping("/existsByName/{name}")
  public ResponseEntity<ApiResponse<Boolean>> existsPermissionByName(@PathVariable String name) {
    return buildSuccessResponse(
        "<Exists Permission Successfully>", permissionService.existsByName(name));
  }

  @Operation(summary = "Find permission by description")
  @GetMapping("/findByDescription/{description}")
  public ResponseEntity<ApiResponse<PermissionResponse>> findPermissionByDescription(
      @PathVariable String description) {
    return buildSuccessResponse(
        "<Find Permission By Description Successfully>",
        permissionService.findByDescription(description));
  }

  @Operation(summary = "Update permission by name")
  @PutMapping("/update/{name}")
  public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(
      @PathVariable String name, @RequestBody @Valid PermissionRequest permissionRequest) {
    return buildSuccessResponse(
        "<Update Permission Successfully>",
        permissionService.updatePermissionByName(name, permissionRequest));
  }

  @Operation(summary = "Delete permission by name")
  @Transactional
  @DeleteMapping("/delete/{name}")
  public ResponseEntity<ApiResponse<Void>> deletePermissionByName(@PathVariable String name) {
    permissionService.deletePermissionByName(name);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(
            ApiResponse.<Void>builder()
                .status(1)
                .message("<Delete Permission Successfully>")
                .build());
  }

  @Operation(summary = "Search permissions by keyword")
  @GetMapping("/keyword/{keyword}")
  public ResponseEntity<ApiResponse<List<PermissionResponse>>> findByKeyword(
      @PathVariable String keyword) {
    return buildSuccessResponse(
        "<Find Permission By Keyword Successfully>", permissionService.searchByKeyword(keyword));
  }
}
