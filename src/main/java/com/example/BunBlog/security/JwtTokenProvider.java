package com.example.BunBlog.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.BunBlog.dto.response.UserReponseDTO;
import com.example.BunBlog.model.User;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider{
    private final JwtEncoder jwtEncoder;
    private final JwtUtil jwtUtil;
    
    // public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public String generateAccessToken(User user, UserReponseDTO userReponseDTO){
        Instant now = Instant.now();
        Instant expiry = now.plus(this.jwtUtil.getAccessTokenExpriration(), ChronoUnit.SECONDS);

        //Permission tạm thời
        List<String> listAuthority = new ArrayList<>();
        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        //Claim
        JwtClaimsSet claims = JwtClaimsSet.builder()
                            .issuedAt(now)
                            .expiresAt(expiry)
                            .subject(user.getId() + "")
                            .claim("user", userReponseDTO)
                            .claim("permission", listAuthority)
                            .build();

        //Header
        JwsHeader jwsHeader = JwsHeader.with(jwtUtil.getJWT_ALGORITHM()).build(); 

        //Final
        String finalJwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        return finalJwt;
    }

    public String generateRefreshToken(User user, UserReponseDTO userReponseDTO){
        Instant now = Instant.now();
        Instant expiry = now.plus(this.jwtUtil.getRefreshTokenExpriration(), ChronoUnit.SECONDS);

        //Claim
        JwtClaimsSet claims = JwtClaimsSet.builder()
                            .issuedAt(now)
                            .expiresAt(expiry)
                            .subject(user.getId() + "")
                            .claim("user", userReponseDTO)
                            .build();
        
        //Header
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build(); 


        //Final
        String finalJwt = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        return finalJwt;
    }
}
