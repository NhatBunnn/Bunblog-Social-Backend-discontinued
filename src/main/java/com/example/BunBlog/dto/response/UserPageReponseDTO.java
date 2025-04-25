package com.example.BunBlog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPageReponseDTO {
    private Long id;
    private String vainityUrl;
    private String mainTitle;
    private UserReponseDTO user;
}
