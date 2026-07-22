package com.sontaypham.todolist.configuration;

import com.sontaypham.todolist.dto.event.publisher.OutboxEventPublisher;
import com.sontaypham.todolist.entities.Outbox;
import com.sontaypham.todolist.enums.OutboxStatus;
import com.sontaypham.todolist.repository.OutboxRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,
        makeFinal = true)
public class OutboxScheduler {

    OutboxRepository outboxRepository;
    List<OutboxEventPublisher> publishers;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<Outbox> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Processing {} pending outbox events...", pendingEvents.size());

        for (Outbox event : pendingEvents) {
            boolean isPublished = false;

            for (OutboxEventPublisher publisher : publishers) {
                if (publisher.supports(event.getEventType())) {
                    try {
                        publisher.publish(event);
                        event.setStatus(OutboxStatus.PROCESSED);
                        outboxRepository.save(event);
                        isPublished = true;
                        break;
                    } catch (Exception e) {
                        log.error("Error executing publisher for event ID: {}", event.getId(), e);
                        event.setStatus(OutboxStatus.FAILED);
                        outboxRepository.save(event);
                        isPublished = true;
                        break;
                    }
                }
            }

            if (! isPublished) {
                log.warn("No suitable outbox publisher found for event type: {}", event.getEventType());
                event.setStatus(OutboxStatus.FAILED);
                outboxRepository.save(event);
            }
        }
    }

}
