package com.sontaypham.todolist.dto.event.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sontaypham.todolist.dto.event.user.UserCreatedEvent;
import com.sontaypham.todolist.dto.response.UserResponse;
import com.sontaypham.todolist.entities.Outbox;
import com.sontaypham.todolist.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreatedOutboxPublisher implements OutboxEventPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String eventType) {
        return "USER_CREATED".equals(eventType);
    }

    @Override
    public void publish(Outbox outboxEvent) {
        try {
            UserResponse userResponse = objectMapper.readValue(outboxEvent.getPayload(), UserResponse.class);
            User user =
                    User.builder()
                        .id(userResponse.getId())
                        .username(userResponse.getUsername())
                        .email(userResponse.getEmail())
                        .build();
            eventPublisher.publishEvent(new UserCreatedEvent(this, user));
            log.info("Successfully published USER_CREATED event for user: {}", user.getUsername());

        } catch (Exception e) {
            log.error("Failed to publish USER_CREATED event", e);
            throw new RuntimeException(e);
        }
    }

}
