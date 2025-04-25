package com.example.BunBlog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.BunBlog.dto.response.PostReponseDTO;
import com.example.BunBlog.model.Post;
import com.example.BunBlog.model.User;
import com.example.BunBlog.repository.PostRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PostService {
    private PostRepository postRepository;
    private UserService userService;

    public void savePost(Post post){
        this.postRepository.save(post);
    }

    public PostReponseDTO convertToPostReponseDTO(Post post){
        PostReponseDTO postReponseDTO = new PostReponseDTO();
        postReponseDTO.setContent(post.getContent());
        postReponseDTO.setId(post.getId());
        postReponseDTO.setUser(this.userService.convertUsertoUserReponseDTO(post.getUser()));
        return postReponseDTO;
    }

    public List<PostReponseDTO> convertToPostReponseDTO(List<Post> posts){
        List<PostReponseDTO> postReponseDTO = new ArrayList<>();
        for (Post post : posts) {
            postReponseDTO.add(this.convertToPostReponseDTO(post));
        }
        return postReponseDTO;
    }
    
    public List<Post> findByUser(User user){
        return this.postRepository.findByUser(user);
    }

}
