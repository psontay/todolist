package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String userName);


  void deleteById(String id);

  void deleteByUsername(String userName);

  Optional<User> findById(@NonNull String id);

  Optional<User> findByEmail(String email);

  @Query(
      """
    SELECT u FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.id) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
  List<User> findByKeyword(@Param("keyword") String keyword);
}
