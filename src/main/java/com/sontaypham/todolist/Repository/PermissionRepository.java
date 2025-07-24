package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String permissionName);

    Optional<Permission> findByName(String permissionName);

    Optional<Permission> findByDescription(String permissionDescription);

    void deletePermissionByName(String name);

    List<Permission> findAllByNameIn(Set<String> permissionName);

    List<Permission> findAllByNameContainingIgnoreCase(String keyword);
}
