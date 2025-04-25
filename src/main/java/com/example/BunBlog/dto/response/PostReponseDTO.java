package com.example.BunBlog.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReponseDTO {
    private Long id;
    private String content;
    private UserReponseDTO user;
}
