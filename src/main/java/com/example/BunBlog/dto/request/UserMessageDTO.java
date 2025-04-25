package com.example.BunBlog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDTO {
    @NotBlank
    private String senderId;

    @NotBlank
    private String receiverId;
}
