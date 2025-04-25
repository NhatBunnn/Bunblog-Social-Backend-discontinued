package com.example.BunBlog.dto.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReponseDTO {
    private String id;
    private String sender;
    private String receiver;
    private String messageContent;
}
