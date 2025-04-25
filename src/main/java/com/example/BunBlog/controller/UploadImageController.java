package com.example.BunBlog.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.BunBlog.dto.request.UserRequestDTO;
import com.example.BunBlog.model.User;
import com.example.BunBlog.security.JwtTokenProvider;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/upload")
public class UploadImageController {
    private SecurityUtil securityUtil;
    private UserService userService;
    private Cloudinary cloudinary;
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> uploadImage(MultipartFile file) throws Exception {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            return ResponseEntity.ok(imageUrl); 
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}