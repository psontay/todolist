package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.entities.Outbox;
import com.sontaypham.todolist.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, String> {

    List<Outbox> findByStatusOrderByCreatedAtAsc(OutboxStatus status);

}
