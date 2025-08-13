package com.sontaypham.todolist.controller.user;

import com.sontaypham.todolist.controller.BaseController;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/search")
@RequiredArgsConstructor
public class UserSearchController extends BaseController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(@RequestParam String keyword) {
        return buildSuccessResponse("Search results", userService.searchUsers(keyword));
    }
}
