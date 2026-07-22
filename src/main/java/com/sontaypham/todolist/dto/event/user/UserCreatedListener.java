package com.sontaypham.todolist.dto.event.user;

import com.sontaypham.todolist.dto.request.NotificationRequest;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.NotificationChannel;
import com.sontaypham.todolist.notification.NotificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreatedListener {

    private final NotificationFactory notificationFactory;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        User user = event.getUser();
        log.info("Starting email delivery check for new user: {}", user.getUsername());
        NotificationRequest request = NotificationRequest.builder()
                                                         .to(user.getEmail())
                                                         .subject("Welcome! Your account has created successfully")
                                                         .templateName("welcome")
                                                         .variables(Map.of("username", user.getUsername()))
                                                         .build();
        notificationFactory.sendNotification(NotificationChannel.EMAIL, request);
    }

}
