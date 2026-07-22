package com.sontaypham.todolist.dto.event;

import com.sontaypham.todolist.entities.EmailDetails;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreatedListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        User user = event.getUser();
        log.info("Starting email delivery check for new user: {}", user.getUsername());
        EmailDetails emailDetails = EmailDetails.builder()
                                                .to(user.getEmail())
                                                .subject("Welcome! Your account has created successfully")
                                                .templateName("welcome")
                                                .variables(Map.of("username", user.getUsername()))
                                                .build();
        emailService.sendTemplateHtmlEmail(emailDetails);
        log.info("Welcome email sent successfully to: {}", user.getEmail());
    }

}
