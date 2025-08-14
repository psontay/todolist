package com.sontaypham.todolist.service;

import com.sontaypham.todolist.dto.request.UserCreationRequest;
import com.sontaypham.todolist.dto.request.UserUpdateRequest;
import com.sontaypham.todolist.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserCreationRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String id);

    UserResponse getUserByEmail(String email);

    void updateUser(String id, UserUpdateRequest request);

    void deleteUser(String id);

    UserResponse assignRoleToUser(String id, String roleName);

    UserResponse updateUserPassword(String id, String oldPassword, String newPassword);

    List<UserResponse> searchUsers(String keyword);

    UserResponse getUserProfile();

    String getCurrentUserId();
}
