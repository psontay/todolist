package com.sontaypham.todolist.Service;

import com.sontaypham.todolist.DTO.Request.TaskUpdateRequest;
import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Request.UserUpdateRequest;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Entities.EmailDetails;
import com.sontaypham.todolist.Entities.Role;
import com.sontaypham.todolist.Entities.Task;
import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Enums.RoleName;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Mapper.UserMapper;
import com.sontaypham.todolist.Repository.EmailRepository;
import com.sontaypham.todolist.Repository.RoleRepository;
import com.sontaypham.todolist.Repository.TaskRepository;
import com.sontaypham.todolist.Repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
  final UserRepository userRepository;
  final PasswordEncoder passwordEncoder;
  final UserMapper userMapper;
  final RoleRepository roleRepository;
  final TaskRepository taskRepository;
  final EmailRepository emailRepository;
  String emailMessageBody = "";

  @CachePut( cacheNames = "user-create" , key = "create" )
  public UserResponse create(UserCreationRequest request) {
    User user = userMapper.toUser(request);
    emailMessageBody =
        "Hello "
            + user.getName()
            + "! Congratulations! Your account has been successfully created on "
            + "TodoList App.\n"
            + "\n"
            + "You can now log in and start managing your tasks more efficiently.\n"
            + "\n"
            + "Wishing you a productive and successful day \uD83D\uDCAA\n"
            + "\n"
            + "Best regards,  \n"
            + "SonTayPham - The TodoList App's Admin";
    EmailDetails emailDetails =
        EmailDetails.builder()
            .to(user.getEmail())
            .messageBody(emailMessageBody)
            .subject("Welcome! Your account has created successfully ✔")
            .build();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    var role =
        roleRepository
            .findByName(RoleName.USER.name())
            .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    user.setRoles(roles);
    try {
      user = userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      log.error(e.getMessage());
      throw new ApiException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }
    emailRepository.sendSimpleMail(emailDetails);
    return userMapper.toUserResponse(user);
  }
  @Cacheable( cacheNames = "user-list" )
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }
  @Cacheable( cacheNames = "user-by-id" , key = "#id")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUserById(String id) {
    return userRepository
        .findById(id)
        .map(userMapper::toUserResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
  }
  @Cacheable( cacheNames = "user-by-email" , key = "#email")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .map(userMapper::toUserResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
  }
  @CacheEvict( cacheNames = "user-by-id" , key = "#id")
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public void updateUser(String id, UserUpdateRequest request) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Set<Task> existingTasks = user.getTasks();
    existingTasks.clear();

    for (TaskUpdateRequest t : request.getTasks()) {
      Task task;

      if (t.getId() != null) {
        task =
            taskRepository
                .findById(t.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.TASK_NOT_FOUND));
        task.setTitle(t.getTitle());
        task.setStatus(t.getStatus());
      } else {
        task = Task.builder().title(t.getTitle()).status(t.getStatus()).build();
      }

      task.setUser(user);
      existingTasks.add(task);
    }

    Set<Role> roles =
        request.getRoles().stream()
            .map(
                roleName ->
                    roleRepository
                        .findByName(roleName)
                        .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND)))
            .collect(Collectors.toSet());
    user.setRoles(roles);
    userRepository.save(user);
  }
  @CacheEvict( cacheNames = "user-by-id" , key = "#id")
  @Transactional
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
  public void deleteUser(String id) {
    if (userRepository.findById(id).isEmpty()) throw new ApiException(ErrorCode.USER_NOT_FOUND);
    userRepository.deleteById(id);
  }

  // advanced service
  @CachePut( cacheNames = "user-by-id" , key = "#id")
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse assignRoleToUser(String id, String roleName) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    Role role =
        roleRepository
            .findByName(roleName)
            .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));

    Set<Role> currentRoles = new HashSet<>(user.getRoles());
    currentRoles.add(role);
    user.setRoles(currentRoles);
    userRepository.save(user);
    return userMapper.toUserResponse(user);
  }
  @CacheEvict( cacheNames = "user-by-id" , key = "#id")
  @Transactional
  @PostAuthorize("returnObject.name == authentication.name")
  public UserResponse updateUserPassword(String id, String oldPassword, String newPassword) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    if (!passwordEncoder.matches(oldPassword, user.getPassword()))
      throw new ApiException(ErrorCode.PASSWORD_NOT_MATCHES);
    user.setPassword(passwordEncoder.encode(newPassword));
    log.warn("Change password success!");
    return userMapper.toUserResponse(user);
  }
  @Cacheable( cacheNames = "user-by-keyword" , key = "#keyword")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> searchUsers(String keyword) {
    return userRepository.findByKeyword(keyword).stream().map(userMapper::toUserResponse).toList();
  }
  @Cacheable( cacheNames = "user-profile" , key = "current")
  public UserResponse getUserProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();
    String userId = jwt.getClaim("userId");
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }
}
