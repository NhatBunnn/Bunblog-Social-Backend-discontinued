package com.example.BunBlog.service;

import org.springframework.stereotype.Service;

import com.example.BunBlog.dto.response.UserPageReponseDTO;
import com.example.BunBlog.model.User;
import com.example.BunBlog.model.UserPage;
import com.example.BunBlog.repository.UserPageRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Service 
public class UserPageService {
    private UserPageRepository userPageRepository;
    private UserService userService;

    public void saveUserPage(UserPage userPage){
        this.userPageRepository.save(userPage);
    }

    public boolean exitsByUser(User user){
        return this.userPageRepository.existsByUser(user);
    }

    public UserPage findByUser(User user){
        return this.userPageRepository.findByUser(user);
    }

    public UserPage findByVainityUrl(String userPage){
        return this.userPageRepository.findByVainityUrl(userPage);
    }

    public UserPageReponseDTO convertUserPageToUserPageReponseDTO(UserPage userPage){
        UserPageReponseDTO userPageReponseDTO = new UserPageReponseDTO();
        userPageReponseDTO.setId(userPage.getId());
        userPageReponseDTO.setVainityUrl(userPage.getVainityUrl());
        userPageReponseDTO.setUser(this.userService.convertUsertoUserReponseDTO(userPage.getUser()));
        return userPageReponseDTO;
    }
}