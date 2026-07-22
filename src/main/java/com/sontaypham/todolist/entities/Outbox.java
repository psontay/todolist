package com.sontaypham.todolist.entities;

import com.sontaypham.todolist.enums.OutboxStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "outbox")
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "aggregate_type",
            nullable = false)
    String aggregateType;
    @Column(name = "aggregate_id",
            nullable = false)
    String aggregateId;
    @Column(name = "event_type",
            nullable = false)
    String eventType;
    @Column(columnDefinition = "TEXT",
            nullable = false)
    String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OutboxStatus status;

    @Column(name = "created_at",
            nullable = false)
    LocalDateTime createdAt;

}
