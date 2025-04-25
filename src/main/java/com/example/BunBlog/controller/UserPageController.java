package com.example.BunBlog.controller;

import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.model.User;
import com.example.BunBlog.model.UserPage;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.service.UserPageService;
import com.example.BunBlog.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class UserPageController {
    private SecurityUtil securityUtil;
    private UserService userService;
    private UserPageService userPageService;
    
    @PostMapping("/createUserPage")
    public ResponseEntity<Object> createUserPage(@RequestBody UserPage userPage) throws Exception{
        String userId = this.securityUtil.getCurrentUser().isPresent() ? this.securityUtil.getCurrentUser().get() : "";
        User user = this.userService.findById(UUID.fromString(userId)).get();

        if(this.userPageService.exitsByUser(user)){
            throw new Exception("Bạn đã có page rồi!");
        }

        if(this.userPageService.findByVainityUrl(userPage.getVainityUrl()) != null){
            throw new Exception("Tên đường dẫn đã tồn tại!");
        }

        UserPage createUserPage = new UserPage();
        createUserPage.setVainityUrl(userPage.getVainityUrl());
        createUserPage.setUser(user);
        this.userPageService.saveUserPage(createUserPage);

        return ResponseEntity.ok()
                             .body(this.userPageService.convertUserPageToUserPageReponseDTO(createUserPage));
    }


    @GetMapping("/UserPage/{VainityUrl}")
    public ResponseEntity<Object> userPage(@PathVariable("VainityUrl") String vainityUrl) throws Exception{

        UserPage userPage = this.userPageService.findByVainityUrl(vainityUrl);
        if(userPage == null){
            throw new Exception("Page của người dùng này chưa tồn tại");
        }

        return ResponseEntity.ok()
                             .body(this.userPageService.
                             convertUserPageToUserPageReponseDTO(userPage));
    }

    @GetMapping("/isExitsUserPage")
    public ResponseEntity<Object> isExitsUserPage() throws Exception{
        String userId = this.securityUtil.getCurrentUser().isPresent() ? this.securityUtil.getCurrentUser().get() : "";
        User user = this.userService.findById(UUID.fromString(userId)).get();

        UserPage userPage = this.userPageService.findByUser(user);
        if(userPage == null){
            throw new Exception("Page của người dùng này chưa tồn tại");
        }

        return ResponseEntity.ok()
                             .body(this.userPageService.convertUserPageToUserPageReponseDTO(userPage));
    }
}
