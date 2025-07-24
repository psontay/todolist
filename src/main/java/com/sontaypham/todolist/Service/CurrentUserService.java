package com.sontaypham.todolist.Service;

import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserService {
  private final UserRepository userRepository;

  public User getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository
        .findByName(username)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
  }
}
