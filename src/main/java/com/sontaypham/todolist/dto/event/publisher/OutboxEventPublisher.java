package com.sontaypham.todolist.dto.event.publisher;

import com.sontaypham.todolist.entities.Outbox;

public interface OutboxEventPublisher {

    boolean supports(String eventType);

    void publish(Outbox outboxEvent);

}
