package com.sontaypham.todolist.controller.permission;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.request.PermissionRequest;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.PermissionResponse;
import com.sontaypham.todolist.service.PermissionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionCrudController extends BaseController {

  private final PermissionService permissionService;

  @PostMapping
  public ResponseEntity<ApiResponse<PermissionResponse>> create(
      @Valid @RequestBody PermissionRequest request) {
    return buildSuccessCreatedResponse(
        "Create Permission Successfully", permissionService.createPermission(request));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<PermissionResponse>>> list() {
    return buildSuccessResponse("List Permission Successfully", permissionService.getPermissions());
  }

  @GetMapping("/{name}")
  public ResponseEntity<ApiResponse<PermissionResponse>> getByName(@PathVariable String name) {
    return buildSuccessResponse("Find Permission Successfully", permissionService.findByName(name));
  }

  @PutMapping("/{name}")
  public ResponseEntity<ApiResponse<PermissionResponse>> update(
      @PathVariable String name, @Valid @RequestBody PermissionRequest request) {
    return buildSuccessResponse(
        "Update Permission Successfully", permissionService.updatePermissionByName(name, request));
  }

  @DeleteMapping("/{name}")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String name) {
    permissionService.deletePermissionByName(name);
    return buildSuccessNoContentResponse("Delete Permission Successfully", null);
  }
}
