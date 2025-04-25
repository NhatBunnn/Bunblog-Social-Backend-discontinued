package com.example.BunBlog.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.BunBlog.model.User;
import com.example.BunBlog.model.UserHistory;
import com.example.BunBlog.repository.UserHistoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserHistoryService {
    private final UserHistoryRepository userHistoryRepository;

    public UserHistory saveUserHistory(UserHistory history) {
        return this.userHistoryRepository.save(history);
    }

    public UserHistory findUserHistoryByUser(User user) {
        return this.userHistoryRepository.findByUser(user);
    }
}
