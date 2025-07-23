package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);

  boolean existsByName(String name);
}
