package com.example.BunBlog.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.BunBlog.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailService implements UserDetailsService{
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.BunBlog.model.User user = this.userService.findByEmail(username);

        return new User(
            user.getEmail(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) 
        );
    }
    
}
