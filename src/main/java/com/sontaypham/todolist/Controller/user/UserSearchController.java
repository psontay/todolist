package com.sontaypham.todolist.Controller.user;

import com.sontaypham.todolist.Controller.BaseController;
import com.sontaypham.todolist.DTO.Response.ApiResponse;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Service.UserService;
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
