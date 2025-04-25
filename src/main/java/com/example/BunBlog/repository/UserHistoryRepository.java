package com.example.BunBlog.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BunBlog.model.User;
import com.example.BunBlog.model.UserHistory;

public interface UserHistoryRepository extends JpaRepository<UserHistory , Long> {
    UserHistory save(UserHistory history);
    UserHistory findByUser(User user);
}
