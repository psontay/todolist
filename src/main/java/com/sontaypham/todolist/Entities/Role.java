package com.sontaypham.todolist.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "permissions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {
  @Id @EqualsAndHashCode.Include String name;
  String description;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinTable(
          name = "role_permission",
          joinColumns = @JoinColumn(name = "role_name"),
          inverseJoinColumns = @JoinColumn(name = "permission_name"))
  Set<Permission> permissions = new HashSet<>();
}
