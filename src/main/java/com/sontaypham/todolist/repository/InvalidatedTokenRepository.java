package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.entities.InvalidatedToken;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
  void deleteByExpTimeBefore(Date now);
}
