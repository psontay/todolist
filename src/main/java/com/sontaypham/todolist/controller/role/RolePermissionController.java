package com.sontaypham.todolist.controller.role;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.RoleResponse;
import com.sontaypham.todolist.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles/{name}/permissions")
@RequiredArgsConstructor
public class RolePermissionController extends BaseController {

    private final RoleService roleService;

    @PutMapping
    public ResponseEntity<ApiResponse<RoleResponse>> addPermissions(
            @PathVariable String name, @RequestBody List<String> permissionsName) {
        return buildSuccessResponse("Permissions added", roleService.addPermissionsToRole(name, permissionsName));
    }
}

