package com.sontaypham.todolist.Entities;

import com.sontaypham.todolist.Enums.TaskStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @EqualsAndHashCode.Include
  String id;

  String title;

  @Enumerated(EnumType.STRING)
  TaskStatus status;

  LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }
}
