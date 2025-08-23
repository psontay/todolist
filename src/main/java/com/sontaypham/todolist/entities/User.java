package com.sontaypham.todolist.entities;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
@ToString(exclude = {"roles", "tasks"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
          @EqualsAndHashCode.Include
  String id;

  @Column(name = "username" , nullable = false, unique = true)
  String username;

  String email;
  String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_name"))
  Set<Role> roles;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<Task> tasks;
}
