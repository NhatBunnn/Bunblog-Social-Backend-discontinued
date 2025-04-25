package com.example.BunBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BunBlog.model.User;
import com.example.BunBlog.model.UserPage;

public interface UserPageRepository extends JpaRepository<UserPage, Long>{
    boolean existsByUser(User user);
    UserPage findByVainityUrl(String vanityUrl);
    UserPage findByUser(User user);
}
