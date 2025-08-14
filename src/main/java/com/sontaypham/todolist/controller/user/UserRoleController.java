package com.sontaypham.todolist.controller.user;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{id}/roles")
@RequiredArgsConstructor
public class UserRoleController extends BaseController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> assignRoleToUser(
      @PathVariable String id, @RequestParam String roleName) {
    return buildSuccessResponse(
        "Role assigned successfully", userService.assignRoleToUser(id, roleName));
  }
}
