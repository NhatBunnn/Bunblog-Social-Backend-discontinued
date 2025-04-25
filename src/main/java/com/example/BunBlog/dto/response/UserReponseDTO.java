package com.example.BunBlog.dto.response;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReponseDTO {
    private UUID id;
    private String email;
    private String username;
    private String avatar;
}
