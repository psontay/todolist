package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.entities.Task;
import com.sontaypham.todolist.entities.User;
import com.sontaypham.todolist.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    Optional<Task> findById(String id);

    @Query("select t from Task t join fetch t.user where t.user.id = :userId")
    List<Task> findByUserIdWithUser(@Param("userId") String userId);

    Optional<Task> findByIdAndUserId(String id, String userId);

    List<Task> findByUserIdAndStatus(String userId, TaskStatus status);

    List<Task> findByUserIdAndTitleContainingIgnoreCase(String userId, String keyword);

    long countByUserId(String userId);

    long countByUserIdAndStatus(String userId, TaskStatus status);

    String user(User user);

    List<Task> findByWarningEmailSentFalseAndDeadlineAfter(LocalDateTime dateTime);

}
