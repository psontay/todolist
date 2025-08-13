package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.entities.Permission;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
  boolean existsByName(String permissionName);

  Optional<Permission> findByName(String permissionName);

  Optional<Permission> findByDescription(String permissionDescription);

  void deletePermissionByName(String name);

  List<Permission> findAllByNameIn(Set<String> permissionName);

  List<Permission> findAllByNameContainingIgnoreCase(String keyword);
}
