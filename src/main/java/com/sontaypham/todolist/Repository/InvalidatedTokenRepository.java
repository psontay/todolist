package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.InvalidatedToken;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
  void deleteByExpTimeBefore(Date now);
}
