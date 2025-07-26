package com.sontaypham.todolist.integration.service;

import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Repository.UserRepository;
import com.sontaypham.todolist.Service.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CurrentUserService currentUserService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                   .id("123")
                   .name("testuser")
                   .email("test@example.com")
                   .password("password")
                   .build();

        // Mock authentication context
        var authentication = new UsernamePasswordAuthenticationToken("testuser", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getCurrentUser_success() {
        // given
        when(userRepository.findByName("testuser")).thenReturn(Optional.of(user));

        // when
        User result = currentUserService.getCurrentUser();

        // then
        assertNotNull(result);
        assertEquals("testuser", result.getName());
        verify(userRepository).findByName("testuser");
    }

    @Test
    void getCurrentUser_userNotFound_throwsException() {
        // given
        when(userRepository.findByName("testuser")).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> currentUserService.getCurrentUser());
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository).findByName("testuser");
    }
}
