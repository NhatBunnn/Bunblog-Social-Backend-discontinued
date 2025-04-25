package com.example.BunBlog.dto.response;

import java.util.List;
import java.util.UUID;

import com.example.BunBlog.security.RefreshToken;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthReponseDTO {
    private UserDTO user;
    private String refreshToken;
    private String accessToken;

    @Getter
    @Setter
    public static class UserDTO{
        private UUID id;
        private String username;
        private String email;
        private String avatar;
    }
    
    //Còn nhiều thuộc tính vcl -> viết sau
    
}
