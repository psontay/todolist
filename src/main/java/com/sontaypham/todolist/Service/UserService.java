package com.sontaypham.todolist.Service;

import com.sontaypham.todolist.DTO.Request.UserCreationRequest;
import com.sontaypham.todolist.DTO.Response.UserResponse;
import com.sontaypham.todolist.Entities.Role;
import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Enums.RoleName;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Mapper.UserMapper;
import com.sontaypham.todolist.Repository.RoleRepository;
import com.sontaypham.todolist.Repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;
  UserMapper userMapper;
  RoleRepository roleRepository;

  public UserResponse create(UserCreationRequest request) {
    User user = userMapper.toUser(request);
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
    return userMapper.toUserResponse(user);
  }
}
