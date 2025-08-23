package com.sontaypham.todolist.configuration;

import com.sontaypham.todolist.entities.Permission;
import com.sontaypham.todolist.entities.Role;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.RoleName;
import com.sontaypham.todolist.exception.ApiException;
import com.sontaypham.todolist.exception.ErrorCode;
import com.sontaypham.todolist.repository.PermissionRepository;
import com.sontaypham.todolist.repository.RoleRepository;
import com.sontaypham.todolist.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationInitConfig {
  private final PasswordEncoder passwordEncoder;

  @Value("${app.default.username}")
  String username;

  @Value("${app.default.password}")
  String password;
    @Value("${app.default.email}")
    String email;
  @Bean
  ApplicationRunner applicationRunner(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PermissionRepository permissionRepository) {
    return args -> {
      log.info("Application started");
      try {
        List<String> defaultPermissions =
            List.of("USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE", "ROLE_MANAGE");
        for (String permission : defaultPermissions) {
          if (permissionRepository.findByName(permission).isEmpty()) {
            permissionRepository.save(new Permission(permission, "Permission for " + permission));
          }
        }
        if (roleRepository.findByName(RoleName.ADMIN.name()).isEmpty()) {
          Set<Permission> permissions = new HashSet<>(permissionRepository.findAll());
          roleRepository.save(
              new Role(RoleName.ADMIN.name(), "Permission for " + RoleName.ADMIN, permissions));
        }
        if (userRepository.findByUsername(username).isEmpty()) {
          Role adminRole =
              roleRepository
                  .findByName(RoleName.ADMIN.name())
                  .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
          User user =
              User.builder()
                  .username(username)
                      .email(email)
                  .password(passwordEncoder.encode(password))
                  .roles(Set.of(adminRole))
                  .build();
          userRepository.save(user);
          log.warn("Admin user has been created w default password");
        }
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    };
  }
}
