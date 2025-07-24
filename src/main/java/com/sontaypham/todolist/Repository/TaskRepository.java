package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.Task;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
  Optional<Task> findById(String id);
}
