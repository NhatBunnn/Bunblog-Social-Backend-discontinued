package com.example.BunBlog.controller;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.request.UserRequestDTO;
import com.example.BunBlog.dto.response.UserReponseDTO;
import com.example.BunBlog.model.User;
import com.example.BunBlog.security.JwtTokenProvider;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/fetchAllUsers")
    public ResponseEntity<List<UserReponseDTO>> fetchAllUsers(){
        List<User> users = this.userService.fetchAllUsers();
        return ResponseEntity.ok()
                             .body(this.userService.convertUsertoUserReponseDTO(users));
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@ModelAttribute UserRequestDTO updateUser) throws Exception{
        User user = this.userService.updateUser(updateUser);
        String accessToken = this.jwtTokenProvider.generateAccessToken(
            user, 
            this.userService.convertUsertoUserReponseDTO(user)
            );
        return ResponseEntity.ok()
                             .body(this.userService.convertUserToAuthReponseDTO(user, null, accessToken));
    }


    
}
