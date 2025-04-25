package com.example.BunBlog.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BunBlog.model.User;
import com.example.BunBlog.repository.RefreshTokenRepository;
import com.example.BunBlog.security.RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service 
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(User user, String refreshToken){
        RefreshToken refreshTokenInstant = new RefreshToken();
        refreshTokenInstant.setRefreshToken(refreshToken);
        refreshTokenInstant.setUser(user);
    
        refreshTokenRepository.save(refreshTokenInstant);
    }

    public List<RefreshToken> getRefreshTokenByUser(User user){
        return this.refreshTokenRepository.findByUser(user);
    }

    @Transactional
    public void removeRefreshToken(User user, String refreshToken){
        this.refreshTokenRepository.deleteByRefreshToken(refreshToken);
        System.out.println("đang xóa refresh token trong db");
    }

    public RefreshToken fetchUserByRefreshTokenAndUserId(String refreshToken, UUID userId){
        return this.refreshTokenRepository.findByRefreshTokenAndUserId(refreshToken, userId);
    }
}
