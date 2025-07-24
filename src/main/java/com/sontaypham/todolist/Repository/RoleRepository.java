package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String roleName);

  boolean existsByName(String roleName);

  Optional<Role> findByDescription(String description);

  void deleteByName(String roleName);

  Set<Role> findByNameIn(Set<String> names);

  List<Role> findAllByNameContainingIgnoreCase(String keyword);
}
