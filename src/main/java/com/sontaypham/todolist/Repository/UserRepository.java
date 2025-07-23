package com.sontaypham.todolist.Repository;

import com.sontaypham.todolist.Entities.User;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByName(String name);

  boolean existsByName(String name);

  void deleteByName(String name);

  Optional<User> findById( @NonNull String id);

  Optional<User> findByEmail(String email);


  @Query(
      """
    SELECT u FROM User u
    WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(u.id) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
  List<User> findByKeyword(@Param("keyword") String keyword);
}

