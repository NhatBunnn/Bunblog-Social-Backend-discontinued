package com.example.BunBlog.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.response.MessageReponseDTO;
import com.example.BunBlog.model.User;
import com.example.BunBlog.repository.ChatMessageRepository;
import com.example.BunBlog.repository.UserRepository;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.service.ChatMessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;


@AllArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private SecurityUtil securityUtil;
    private ObjectMapper objectMapper;
    private ChatMessageService chatMessageService;
    private UserRepository userRepository;

    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        Object userId = session.getAttributes().get("userId");
        if(userId != null){
            userSessions.put(userId.toString(), session);
            System.out.println(consoleSyntax.suffix + "Kết nối WebSocket thành công với người dùng: " + userId.toString());  
        }
    } 

    @Override
    //"session" đại diện cho kết nối WebSocket giữa client và server.
    //"message" đại diện cho từng tin nhắn mà client gửi.
    //TextMessage: Đang nhận chuỗi String (và có thể nhận Json)
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        System.out.println("Received JSON: " + payload);
        
        JsonNode jsonNode = objectMapper.readTree(payload); 
        
        String sender = jsonNode.get("sender_id").asText();
        String receiver = jsonNode.get("receiver_id").asText();
        String messageContent = jsonNode.get("message_content").asText();

        MessageReponseDTO chatMessageReponseDTO = new MessageReponseDTO();
        Optional<User> senderUser = this.userRepository.findById(UUID.fromString(sender));

        chatMessageReponseDTO.setId(sender);
        chatMessageReponseDTO.setSender(senderUser.get().getUsername());
        chatMessageReponseDTO.setMessageContent(messageContent);

        // System.out.println(consoleSyntax.suffix + chatMessage.getSender());
        // System.out.println(consoleSyntax.suffix + chatMessage.getMessageContent());


        String jsonMessage = objectMapper.writeValueAsString(chatMessageReponseDTO); 
        
        try {
            WebSocketSession receiverSession = userSessions.get(receiver); 
            receiverSession.sendMessage(new TextMessage(jsonMessage));

            try {
                
                ChatMessage messageStorage = new ChatMessage();
                User senderId = new User();
                User receiverId = new User();
                senderId.setId(UUID.fromString(sender));
                receiverId.setId(UUID.fromString(receiver));
    
                messageStorage.setSender(senderId);
                messageStorage.setReceiver(receiverId);
                messageStorage.setMessageContent(messageContent);
    
                chatMessageService.saveChatMessage(messageStorage);
            } catch (Exception e) {
                System.out.println(consoleSyntax.suffix + "Lỗi: sender hoặc receiver không phải UUID hợp lệ - " + e.getMessage());
            }

        } catch (Exception e) {
            throw new Exception("Người dùng chưa online");
        }

        System.out.println(consoleSyntax.suffix + "Người dùng " + sender +  " đã gửi tin nhắn " + messageContent + " tới " + receiver);
    }
   
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // String username = getUsernameFromSession(session);
        // if (username != null) {
        //     userSessions.remove(username);
        // }
    }
}
