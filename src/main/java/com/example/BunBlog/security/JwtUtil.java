package com.example.BunBlog.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Getter
// @Setter
@Component
public class JwtUtil {
    private SecretKey secretKey;
    private MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512 ;

    @Value("${demo.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpriration;

    @Value("${demo.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpriration;

    @Value("${demo.jwt.base64-secret}")
    private String jwtKey;

    public Jwt decodeToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
        .withSecretKey(getSecretKey())
        .macAlgorithm(JWT_ALGORITHM)
        .build();
        try {
            return jwtDecoder.decode(token);
       } catch (Exception e) {
           System.out.println(">>> Refresh Token error: " + e.getMessage());
           throw e;
       }
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, MacAlgorithm.HS512.getName());
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public MacAlgorithm getJWT_ALGORITHM() {
        return JWT_ALGORITHM;
    }

    public void setJWT_ALGORITHM(MacAlgorithm jWT_ALGORITHM) {
        JWT_ALGORITHM = jWT_ALGORITHM;
    }

    public long getAccessTokenExpriration() {
        return accessTokenExpriration;
    }

    public void setAccessTokenExpriration(long accessTokenExpriration) {
        this.accessTokenExpriration = accessTokenExpriration;
    }

    public long getRefreshTokenExpriration() {
        return refreshTokenExpriration;
    }

    public void setRefreshTokenExpriration(long refreshTokenExpriration) {
        this.refreshTokenExpriration = refreshTokenExpriration;
    }

    public String getJwtKey() {
        return jwtKey;
    }

    public void setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
    }

   

    

    
}
