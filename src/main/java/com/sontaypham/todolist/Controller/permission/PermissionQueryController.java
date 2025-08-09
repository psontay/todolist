package com.sontaypham.todolist.Controller.permission;
import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.PermissionResponse;
import com.sontaypham.todolist.Service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionQueryController extends BaseController {

    private final PermissionService permissionService;

    @GetMapping("/{name}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsByName(@PathVariable String name) {
        return buildSuccessResponse("Exists Permission Successfully", permissionService.existsByName(name));
    }

    @GetMapping(params = "description")
    public ResponseEntity<ApiResponse<PermissionResponse>> findByDescription(@RequestParam String description) {
        return buildSuccessResponse("Find Permission By Description Successfully", permissionService.findByDescription(description));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> search(@RequestParam String keyword) {
        return buildSuccessResponse("Find Permission By Keyword Successfully", permissionService.searchByKeyword(keyword));
    }
}

