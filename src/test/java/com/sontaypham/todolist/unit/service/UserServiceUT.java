package com.sontaypham.todolist.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sontaypham.todolist.dto.request.TaskUpdateRequest;
import com.sontaypham.todolist.dto.request.UserCreationRequest;
import com.sontaypham.todolist.dto.request.UserUpdateRequest;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.entities.Role;
import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.RoleName;
import com.sontaypham.todolist.enums.TaskStatus;
import com.sontaypham.todolist.exception.ApiException;
import com.sontaypham.todolist.exception.ErrorCode;
import com.sontaypham.todolist.mapper.UserMapper;
import com.sontaypham.todolist.repository.RoleRepository;
import com.sontaypham.todolist.repository.UserRepository;
import com.sontaypham.todolist.service.UserService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("/test.properties")
public class UserServiceUT {
  @InjectMocks UserService userService;

  @Mock UserMapper userMapper;

  @Mock UserRepository userRepository;

  @Mock PasswordEncoder passwordEncoder;

  @Mock RoleRepository roleRepository;

  User user;
  UserResponse userResponse;
  UserCreationRequest userCreationRequest;
  Role userRole, adminRole;
  Task existingTask;
  TaskUpdateRequest existingTaskRequest, newTaskRequest;
  UserUpdateRequest userUpdateRequest;

  @BeforeEach
  void setUp() {

    existingTaskRequest =
        TaskUpdateRequest.builder()
            .id("task1")
            .title("Updated Title")
            .status(TaskStatus.COMPLETED)
            .build();
    newTaskRequest =
        TaskUpdateRequest.builder().title("New Task").status(TaskStatus.IN_PROGRESS).build();
    userRole = Role.builder().name(RoleName.USER.name()).build();
    adminRole = Role.builder().name(RoleName.ADMIN.name()).build();
    user =
        User.builder()
            .id("sontaypham")
            .name("Test")
            .email("user@test@gmail.com")
            .password("irrelevant")
            .roles(Set.of(Role.builder().name(RoleName.USER.name()).build()))
            .build();
    user.setTasks(new HashSet<>());
    user.setRoles(new HashSet<>());
    existingTask =
        Task.builder().id("task1").title("Old Title").status(TaskStatus.PENDING).user(user).build();
    userCreationRequest =
        UserCreationRequest.builder()
            .name("Test")
            .email("user@test@gmail.com")
            .password("irrelevant")
            .build();
    userResponse =
        UserResponse.builder()
            .id("sontaypham")
            .name("Test")
            .email("user@test@gmail.com")
            .roles(Set.of(adminRole.getName(), userRole.getName()))
            .build();

    userUpdateRequest =
        UserUpdateRequest.builder()
            .name("TestUpdate")
            .email("user@update@gmail.com")
            .password("updated")
            .roles(Set.of(RoleName.USER.name(), RoleName.ADMIN.name()))
            .tasks(Set.of(existingTaskRequest, newTaskRequest))
            .build();
  }

  @Test
  void getUserProfile_userNotFound_throwsException() {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaim("userId")).thenReturn("sontaypham");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(jwt);

    org.springframework.security.core.context.SecurityContext securityContext =
        mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findByName("sontaypham")).thenReturn(Optional.empty());

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              userService.getUserProfile();
            });

    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }
}
