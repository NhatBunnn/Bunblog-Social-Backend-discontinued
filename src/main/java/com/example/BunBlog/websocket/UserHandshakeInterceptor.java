package com.example.BunBlog.websocket;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.example.BunBlog.security.SecurityUtil;

public class UserHandshakeInterceptor implements HandshakeInterceptor {
    private final SecurityUtil securityUtil;

    public UserHandshakeInterceptor(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        
         if (request instanceof ServletServerHttpRequest servletRequest){
            var httpRequest = servletRequest.getServletRequest();
            String accessToken = httpRequest.getParameter("accessToken");
            System.out.println("in ra access token: " + accessToken);
            if(accessToken != null){
                try {
                    Jwt decodedJwt = this.securityUtil.jwtDecoder(accessToken);
                    String userId = decodedJwt.getSubject();
                    UUID uuid = UUID.fromString(userId);
                    attributes.put("userId", uuid);
                } catch (Exception e) {
                    System.out.println("Invalid JWT: " + e.getMessage());
                    return false; 
                }
            }
         }
         return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afterHandshake'");
    }
    
}
