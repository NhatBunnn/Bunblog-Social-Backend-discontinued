package com.example.BunBlog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BunBlog.security.RefreshToken;
import com.example.BunBlog.model.User;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    RefreshToken save(RefreshToken refreshToken);
    List<RefreshToken> findByUser(User user);
    void deleteByRefreshToken(String refreshToken);
    //SỬA SAU USER_ID
    RefreshToken findByRefreshTokenAndUserId(String refreshToken, UUID userId);
}
