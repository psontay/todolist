package com.sontaypham.todolist.integration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.sontaypham.todolist.repository.TaskRepository;
import com.sontaypham.todolist.repository.UserRepository;
import com.sontaypham.todolist.service.impl.UserServiceImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImplIT {
  @Autowired UserServiceImpl userServiceImpl;
  @Autowired MockMvc mockMvc;
  @MockBean UserRepository userRepository;
  @MockBean UserMapper userMapper;
  @MockBean PasswordEncoder passwordEncoder;
  @MockBean RoleRepository roleRepository;
  @MockBean TaskRepository taskRepository;
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
            .username("Test")
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
            .username("Test")
            .email("user@test@gmail.com")
            .password("irrelevant")
            .build();
    userResponse =
        UserResponse.builder()
            .id("sontaypham")
            .username("Test")
            .email("user@test@gmail.com")
            .roles(Set.of(adminRole.getName(), userRole.getName()))
            .build();

    userUpdateRequest =
        UserUpdateRequest.builder()
            .username("TestUpdate")
            .email("user@update@gmail.com")
            .password("updated")
            .roles(Set.of(RoleName.USER.name(), RoleName.ADMIN.name()))
            .tasks(Set.of(existingTaskRequest, newTaskRequest))
            .build();
  }

  @Test
  @WithMockUser(
      username = "Test",
      authorities = {"USER_CREATE"})
  void createUser_validRequest_success() {
    // given
    when(userMapper.toUser(userCreationRequest)).thenReturn(user);
    when(passwordEncoder.encode("irrelevant")).thenReturn("encodedPassword");
    when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.of(userRole));

    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);

    // when
    UserResponse result = userServiceImpl.create(userCreationRequest);

    // then
    assertNotNull(result);
    assertEquals("Test", result.getUsername());
    assertEquals("user@test@gmail.com", result.getEmail());
    verify(userMapper).toUser(userCreationRequest);
    verify(passwordEncoder).encode("irrelevant");
    verify(roleRepository).findByName(RoleName.USER.name());
    verify(userRepository).save(user);
    verify(userMapper).toUserResponse(user);
  }

  @Test
  void createUser_usernameExists_throwsException() {
    when(userMapper.toUser(userCreationRequest)).thenReturn(user);
    when(passwordEncoder.encode("irrelevant")).thenReturn("encodedPassword");
    when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.of(userRole));
    when(userRepository.save(any(User.class)))
        .thenThrow(new DataIntegrityViolationException("duplicate key"));
    ApiException exception =
        assertThrows(ApiException.class, () -> userServiceImpl.create(userCreationRequest));
    assertEquals("Username already exists", exception.getMessage());
  }

  @Test
  void createUser_roleNotFound_throwsException() {
    when(userMapper.toUser(userCreationRequest)).thenReturn(user);
    when(passwordEncoder.encode("irrelevant")).thenReturn("encodedPassword");
    when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.empty());

    ApiException exception =
        assertThrows(ApiException.class, () -> userServiceImpl.create(userCreationRequest));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getAllUsers_success() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);
    List<UserResponse> ans = userServiceImpl.getAllUsers();
    assertEquals(1, ans.size());
    assertEquals(userResponse.getEmail(), ans.getFirst().getEmail());
    verify(userRepository).findAll();
    verify(userMapper).toUserResponse(user);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getUserById_success() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);
    UserResponse result = userServiceImpl.getUserById("sontaypham");
    assertEquals(userResponse.getEmail(), result.getEmail());
    verify(userRepository).findById("sontaypham");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getUserById_userNotFound_throwsException() {
    when(userRepository.findById("unk")).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(ApiException.class, () -> userServiceImpl.getUserById("unk"));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getUserByEmail_success() {
    when(userRepository.findByEmail("user@test@gmail.com")).thenReturn(Optional.of(user));
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);
    UserResponse result = userServiceImpl.getUserByEmail("user@test@gmail.com");
    assertEquals(userResponse.getEmail(), result.getEmail());
    verify(userRepository).findByEmail("user@test@gmail.com");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getUserByEmail_userNotFound_throwsException() {
    when(userRepository.findByEmail("user@unk@gmail.com")).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(
            ApiException.class, () -> userServiceImpl.getUserByEmail("user@unk@gmail.com"));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateUser_success() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("updated")).thenReturn("encodedPassword");
    when(taskRepository.findById("task1")).thenReturn(Optional.of(existingTask));
    when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.of(userRole));
    when(roleRepository.findByName(RoleName.ADMIN.name())).thenReturn(Optional.of(adminRole));
    userServiceImpl.updateUser("sontaypham", userUpdateRequest);
    assertEquals("TestUpdate", user.getUsername());
    assertEquals("user@update@gmail.com", user.getEmail());
    assertEquals("encodedPassword", user.getPassword());

    assertEquals(2, user.getTasks().size());
    assertTrue(user.getTasks().stream().anyMatch(t -> t.getTitle().equals("Updated Title")));
    assertTrue(user.getTasks().stream().anyMatch(t -> t.getTitle().equals("New Task")));

    assertEquals(2, user.getRoles().size());

    verify(userRepository).save(user);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateUser_userNotFound_throwsException() {
    when(userRepository.findById("unk")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("irrelevant")).thenReturn("encodedPassword");
    ApiException exception =
        assertThrows(
            ApiException.class, () -> userServiceImpl.updateUser("unk", userUpdateRequest));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateUser_taskNotFound_throwsException() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("irrelevant")).thenReturn("encodedPassword");
    when(taskRepository.findById("unk")).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(
            ApiException.class, () -> userServiceImpl.updateUser("sontaypham", userUpdateRequest));
    assertEquals(ErrorCode.TASK_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateUser_roleNotFound_throwsException() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("irrelevant")).thenReturn("encodedPassword");
    when(taskRepository.findById("task1")).thenReturn(Optional.of(existingTask));
    when(roleRepository.findByName(RoleName.ADMIN.name())).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(
            ApiException.class, () -> userServiceImpl.updateUser("sontaypham", userUpdateRequest));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteUser_success() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    userServiceImpl.deleteByUsername("sontaypham");
    assertEquals(0, userRepository.findAll().size());
    verify(userRepository).deleteById("sontaypham");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteUser_userNotFound_throwsException() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(ApiException.class, () -> userServiceImpl.deleteByUsername("sontaypham"));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void assignRoleToUser() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.of(userRole));
    when(roleRepository.findByName(RoleName.ADMIN.name())).thenReturn(Optional.of(adminRole));
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);
    UserResponse ans = userServiceImpl.assignRoleToUser("sontaypham", RoleName.ADMIN.name());
    assertEquals(2, ans.getRoles().size());
    verify(userRepository).findById("sontaypham");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void assignRoleToUser_userNotFound_throwsException() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.empty());
    ApiException apiException =
        assertThrows(
            ApiException.class,
            () -> userServiceImpl.assignRoleToUser("sontaypham", RoleName.USER.name()));
    assertEquals(ErrorCode.USER_NOT_FOUND, apiException.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void assignRoleToUser_roleNotFound_throwsException() {
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(roleRepository.findByName(RoleName.USER.name())).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> userServiceImpl.assignRoleToUser("sontaypham", RoleName.USER.name()));
    assertEquals(ErrorCode.ROLE_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(username = "Test")
  void updateUserPassword_success() {
    // given
    String oldPassword = "irrelevant";
    String newPassword = "updated";

    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(true);
    when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

    when(userMapper.toUserResponse(user)).thenReturn(userResponse);

    // when
    UserResponse ans = userServiceImpl.updateUserPassword("sontaypham", oldPassword, newPassword);

    // then
    assertNotNull(ans);
    assertEquals(userResponse.getEmail(), ans.getEmail());
    assertEquals("encodedPassword", user.getPassword());
    verify(userRepository).findById("sontaypham");
    verify(passwordEncoder).matches("irrelevant", "irrelevant");
    verify(passwordEncoder).encode("updated");
    verify(userMapper).toUserResponse(user);
  }

  @Test
  @WithMockUser(username = "Test")
  void updateUserPassword_userNotFound_throwsException() {
    String oldPassword = "irrelevant";
    String newPassword = "updated";
    when(userRepository.findById("sontaypham")).thenReturn(Optional.empty());
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> userServiceImpl.updateUserPassword("sontaypham", oldPassword, newPassword));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  @WithMockUser(username = "Test")
  void updateUserPassword_oldPasswordWrong_throwsException() {
    String oldPassword = "unk";
    String newPassword = "updated";
    when(userRepository.findById("sontaypham")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> userServiceImpl.updateUserPassword("sontaypham", oldPassword, newPassword));
    assertEquals(ErrorCode.PASSWORD_NOT_MATCHES, exception.getErrorCode());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void searchUsers_success() {
    when(userRepository.findByKeyword("sontaypham")).thenReturn(List.of(user));
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);
    List<UserResponse> ans = userServiceImpl.searchUsers("sontaypham");
    assertEquals(1, ans.size());
    assertEquals(userResponse.getEmail(), ans.getFirst().getEmail());
    verify(userRepository).findByKeyword("sontaypham");
  }

  @Test
  @WithMockUser(username = "Test", roles = "USER")
  void getUserProfile_success() throws Exception {
    when(userRepository.findByUsername("sontaypham")).thenReturn(Optional.of(user));
    when(userMapper.toUserResponse(user)).thenReturn(userResponse);
    mockMvc
        .perform(get("/users/profile").with(jwt().jwt(jwt -> jwt.claim("userId", "sontaypham"))))
        .andExpect(status().isOk())
        .andReturn();
  }
}
