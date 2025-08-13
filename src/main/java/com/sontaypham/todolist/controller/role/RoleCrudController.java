package com.sontaypham.todolist.controller.role;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.request.RoleRequest;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.RoleResponse;
import com.sontaypham.todolist.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleCrudController extends BaseController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest roleRequest) {
        return buildSuccessCreatedResponse("Role created", roleService.create(roleRequest));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        return buildSuccessResponse("All roles fetched", roleService.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<RoleResponse>> getByName(@PathVariable String name) {
        return buildSuccessResponse("Role found", roleService.findByName(name));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(@PathVariable String name, @RequestBody RoleRequest roleRequest) {
        return buildSuccessResponse("Role updated", roleService.updateFromRequest(roleRequest));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String name) {
        roleService.deleteByName(name);
        return buildSuccessNoContentResponse("Role deleted", null);
    }
}

