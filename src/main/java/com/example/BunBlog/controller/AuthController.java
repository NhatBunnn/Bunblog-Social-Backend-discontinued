package com.example.BunBlog.controller;


import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BunBlog.config.consoleSyntax;
import com.example.BunBlog.dto.request.UserRequestDTO;
import com.example.BunBlog.exception.CustomExceptions.UsernameNotFoundException;
import com.example.BunBlog.model.User;
import com.example.BunBlog.security.JwtTokenProvider;
import com.example.BunBlog.security.SecurityUtil;
import com.example.BunBlog.security.JwtUtil;
import com.example.BunBlog.service.RefreshTokenService;
import com.example.BunBlog.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final SecurityUtil securityUtil;


    @PostMapping("/auth/register")
    public User createUser(@Valid @RequestBody User user) throws Exception{
        boolean isEmailExit = userService.existsByEmail(user.getEmail());
        
        if(isEmailExit){
            throw new Exception("Email đã tồn tại");
        }

        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        this.userService.saveUser(user);

        return null;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody UserRequestDTO userRequestDTO) throws RuntimeException{
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
            = new UsernamePasswordAuthenticationToken(userRequestDTO.getEmail(), userRequestDTO.getPassword());
            authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
        } catch (Exception e) {
            throw new UsernameNotFoundException("Validation failed");
        }


        User user = this.userService.findByEmail(authentication.getName());

        String accessToken = this.jwtTokenProvider.generateAccessToken(
            user, 
            this.userService.convertUsertoUserReponseDTO(user)
            );
       
        String refreshToken = this.jwtTokenProvider.generateRefreshToken(
            user, 
            this.userService.convertUsertoUserReponseDTO(user)
            );

        this.refreshTokenService.saveRefreshToken(user, refreshToken);
        
        ResponseCookie responseCookie = ResponseCookie
            .from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(jwtUtil.getRefreshTokenExpriration())
            .sameSite("None")
            .build();

        
        return ResponseEntity.ok()
                             .header("Set-Cookie", responseCookie.toString())
                             .body(userService.convertUserToAuthReponseDTO(user, refreshToken ,accessToken));

    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Object> logout(@CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) throws Exception{
        // String userId = this.securityUtil.getCurrentUser().isPresent() ? this.securityUtil.getCurrentUser().get() : "";
        
        // if (userId.equals("")) {
        //     throw new IdInvalidException("Access Token không hợp lệ");
        // }

        if(refreshToken.equals("")){
            throw new Exception("Access Token không hợp lệ");
        }

        System.out.println("đây là refreshtoken: " + refreshToken);

        // isValid?
        Jwt decodedJwt = this.securityUtil.jwtDecoder(refreshToken);
        String userId = decodedJwt.getSubject();
        UUID uuid = UUID.fromString(userId);
    
        Optional<User> user = this.userService.findById(uuid);
        
        this.refreshTokenService.removeRefreshToken(user.get(), refreshToken); 
        
        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                            .header("Set-Cookie", deleteSpringCookie.toString())
                             .body("đang xuất thành cong");
    }

    @GetMapping("/auth/refreshToken")
    public ResponseEntity<Object> refreshToken(@CookieValue(name = "refresh_token", defaultValue = "no_Token") String refreshToken) {
        if(refreshToken == "no_Token"){
            return null;
        }    

        Jwt decodedJwt = this.securityUtil.jwtDecoder(refreshToken);
        String userId = decodedJwt.getSubject();
        UUID uuid = UUID.fromString(userId);

        Optional<User> currentUser = this.userService.findById(uuid);

        if(!currentUser.isPresent() && this.refreshTokenService.fetchUserByRefreshTokenAndUserId(refreshToken, uuid) == null){
            return null;
        }

        if(currentUser.get() == null){
            return null;
        }

        this.refreshTokenService.removeRefreshToken(currentUser.get(), refreshToken); 

        String accessToken = this.jwtTokenProvider.generateAccessToken(
            currentUser.get(),
            this.userService.convertUsertoUserReponseDTO(currentUser.get())
            );
        String newRefreshToken = this.jwtTokenProvider.generateRefreshToken(
            currentUser.get(),
            this.userService.convertUsertoUserReponseDTO(currentUser.get())
            );
        
        this.refreshTokenService.saveRefreshToken(currentUser.get(), refreshToken);
        
        ResponseCookie responseCookie = ResponseCookie
            .from("refresh_token", newRefreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(jwtUtil.getRefreshTokenExpriration())
            .build();

        return ResponseEntity.ok()
                             .header("Set-Cookie", responseCookie.toString())
                             .body(userService.convertUserToAuthReponseDTO(currentUser.get(), newRefreshToken ,accessToken));

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") String id){
        Optional<User> user = Optional.of(new User());
        UUID uuid = UUID.fromString(id);
        user = this.userService.findById(uuid);
        return ResponseEntity.ok()
                             .body(this.userService.convertUsertoUserReponseDTO(user.get()));
    }


}
