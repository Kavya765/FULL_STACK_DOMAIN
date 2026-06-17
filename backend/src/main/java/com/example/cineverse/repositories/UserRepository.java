package com.example.cineverse.repositories;

import com.example.cineverse.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    int deleteByUsername(String username);

    boolean existsByUsername(String username);

    User getUserById(int id);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO users (username, email, password) VALUES (:username, :email, :password)",
            nativeQuery = true
    )
    void register(String username, String password, String email);

    boolean existsByEmail(String email);

    User findById(int id);
}
