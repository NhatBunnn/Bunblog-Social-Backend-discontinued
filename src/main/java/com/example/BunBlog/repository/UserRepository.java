package com.example.BunBlog.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BunBlog.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{
    User save(User user);
    void deleteById(UUID userId);
    User findByEmail(String email);
    User findByUsername(String username);
    Optional<User> findById(UUID userId);
    boolean existsByEmail(String email);
}
