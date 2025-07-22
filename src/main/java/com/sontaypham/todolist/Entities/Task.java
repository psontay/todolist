package com.sontaypham.todolist.Entities;

import com.sontaypham.todolist.Enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    String id;
    String title;
    @Enumerated(EnumType.STRING)
    TaskStatus status;
    LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn( name = "user_id")
    User user;
}
