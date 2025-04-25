package com.example.BunBlog.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.model.Post;
import com.example.BunBlog.model.PostMedia;
import com.example.BunBlog.model.User;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.service.PostService;
import com.example.BunBlog.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class PostController {
    private PostService postService;
    private SecurityUtil securityUtil;
    private UserService userService;
    private Cloudinary cloudinary;

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestPart("post") Post post, @RequestPart("postMedia") MultipartFile postMedia) throws IOException{
        String userId = this.securityUtil.getCurrentUser().isPresent() ? this.securityUtil.getCurrentUser().get() : "";
        User user = this.userService.findById(UUID.fromString(userId)).get();

        Post currentPost = new Post();
        currentPost.setContent(post.getContent());
        currentPost.setUser(user);
        this.postService.savePost(currentPost);

        PostMedia currentPostMedia = new PostMedia();
        try {
            Map uploadResult = cloudinary.uploader().upload(postMedia.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            
            currentPostMedia.setImage(imageUrl);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        currentPostMedia.setPost(currentPost);
        
        return ResponseEntity.ok()
                            .body(null);

    }
    
    @PostMapping("/fetchAllPosts")
    public ResponseEntity<?> fetchAllPosts(@RequestBody User user){
        List<Post> post = this.postService.findByUser(user);
        return ResponseEntity.ok()
                            .body(this.postService.convertToPostReponseDTO(post));
    }

    
}
