package com.sontaypham.todolist.Entities;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
  @Id String name;
  String description;

  @ManyToMany( fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "role_name"),
      inverseJoinColumns = @JoinColumn(name = "permission_name"))
  Set<Permission> permissions;
}
