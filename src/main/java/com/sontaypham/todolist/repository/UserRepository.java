package com.sontaypham.todolist.repository;

import com.sontaypham.todolist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String userName);

    void deleteByUsername(String userName);

    Optional<User> findById(@NonNull String id);

    void deleteById(String id);

    Optional<User> findByEmail(String email);

    @Query(
            """
                    SELECT u FROM User u
                    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    """)
    List<User> findByKeyword(@Param("keyword") String keyword);

}
