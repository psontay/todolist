package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByName(String name);
}
