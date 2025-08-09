package com.sontaypham.todolist.Controller.role;

import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.RoleResponse;
import com.sontaypham.todolist.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleQueryController extends BaseController {

    private final RoleService roleService;

    @GetMapping("/{name}/exists")
    public ResponseEntity<ApiResponse<Boolean>> exists(@PathVariable String name) {
        return buildSuccessResponse("Exists check done", roleService.existsByName(name));
    }

    @GetMapping(params = "description")
    public ResponseEntity<ApiResponse<RoleResponse>> findByDescription(@RequestParam String description) {
        return buildSuccessResponse("Role found by description", roleService.findByDescription(description));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> search(@RequestParam String keyword) {
        return buildSuccessResponse("Search result", roleService.findAllByNameIgnoreCase(keyword));
    }
}
