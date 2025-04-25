package com.example.BunBlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BunBlog.model.Post;
import com.example.BunBlog.model.User;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByUser(User user);
}
