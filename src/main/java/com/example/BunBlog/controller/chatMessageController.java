package com.example.BunBlog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.request.UserMessageDTO;
import com.example.BunBlog.repository.ChatMessageRepository;
import com.example.BunBlog.service.ChatMessageService;
import com.example.BunBlog.websocket.ChatMessage;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class chatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("/fetchAllMessages")
    public ResponseEntity<Object> fetchAllChatMessages(@RequestBody UserMessageDTO userMessageDTO){
        List<ChatMessage> messages = this.chatMessageService.fetchMessagesByUserId(userMessageDTO.getSenderId(), userMessageDTO.getReceiverId());
        return ResponseEntity.ok()
                            .body(this.chatMessageService.convetChatMessageTOcChatMessageReponseDTO(messages))                    
        ;
    }

}
