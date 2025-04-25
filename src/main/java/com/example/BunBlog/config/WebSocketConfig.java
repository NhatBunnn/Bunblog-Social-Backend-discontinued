package com.example.BunBlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.BunBlog.repository.UserRepository;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.service.ChatMessageService;
import com.example.BunBlog.websocket.ChatWebSocketHandler;
import com.example.BunBlog.websocket.UserHandshakeInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;



@AllArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private SecurityUtil securityUtil;
    private ObjectMapper objectMapper;
    private ChatMessageService chatMessageService;
        private UserRepository userRepository;

    // Nó có nghĩa là mọi request kết nối đến /chat sẽ được xử lý như một WebSocket, 
    // không phải request HTTP thông thường nữa.

    //Giao thức của websocket là: ws:// thay vì: https://
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
        .addHandler(chatWebSocketHandler(), "/chat")
        .addInterceptors(new UserHandshakeInterceptor(securityUtil))
        .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler(securityUtil, objectMapper, chatMessageService, userRepository);
    }

}
