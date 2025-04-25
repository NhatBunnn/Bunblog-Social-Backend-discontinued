package com.example.BunBlog.service;

import org.springframework.stereotype.Service;

import com.example.BunBlog.model.PostMedia;
import com.example.BunBlog.repository.postMediaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class postMediaService {
    private postMediaRepository postMediaRepository;

    public PostMedia savePostMedia(PostMedia postMedia){
        return this.postMediaRepository.save(postMedia);
    }
}
