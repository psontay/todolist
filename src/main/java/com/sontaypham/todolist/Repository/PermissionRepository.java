package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    List<Permission> findByName(String name);
}
