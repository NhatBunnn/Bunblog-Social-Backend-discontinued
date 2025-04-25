package com.example.BunBlog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.response.ListMessagesReponseDTO;
import com.example.BunBlog.dto.response.MessageReponseDTO;
import com.example.BunBlog.dto.response.UserReponseDTO;
import com.example.BunBlog.model.User;
import com.example.BunBlog.repository.ChatMessageRepository;
import com.example.BunBlog.websocket.ChatMessage;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service 
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatMessage saveChatMessage(ChatMessage chatMessage){
        return this.chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> fetchMessagesByUserId(String senderId, String receiverId){
        List<ChatMessage> messages =  this.chatMessageRepository.
        findBySenderIdAndReceiverId(UUID.fromString(senderId), UUID.fromString(receiverId));
        return messages;
    }

    public ListMessagesReponseDTO convetChatMessageTOcChatMessageReponseDTO(List<ChatMessage> messages){
        List<MessageReponseDTO> listMessages = new ArrayList<MessageReponseDTO>();

        for (ChatMessage chatMessage : messages) {
            MessageReponseDTO messageReponseDTO = new MessageReponseDTO();
            messageReponseDTO.setSender(chatMessage.getSender().getId().toString());
            messageReponseDTO.setReceiver(chatMessage.getReceiver().getId().toString());
            messageReponseDTO.setMessageContent(chatMessage.getMessageContent());
            listMessages.add(messageReponseDTO);
        }

        ListMessagesReponseDTO listMessagesReponseDTO = new ListMessagesReponseDTO();
        listMessagesReponseDTO.setMessageReponseDTO(listMessages);
        return listMessagesReponseDTO;
    }


    
}


