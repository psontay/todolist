package com.sontaypham.todolist.controller.role;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.RoleResponse;
import com.sontaypham.todolist.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<ApiResponse<RoleResponse>> findByDescription(
      @RequestParam String description) {
    return buildSuccessResponse(
        "Role found by description", roleService.findByDescription(description));
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<RoleResponse>>> search(@RequestParam String keyword) {
    return buildSuccessResponse("Search result", roleService.findAllByNameIgnoreCase(keyword));
  }
}
