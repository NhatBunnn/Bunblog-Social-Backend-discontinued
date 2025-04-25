package com.example.BunBlog.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_history")
@Entity
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String old_password;

    private LocalDateTime create_account_at;

    private LocalDateTime change_time_username;
    private LocalDateTime change_time_email;
    private LocalDateTime change_time_date_of_birth; 
    private LocalDateTime change_time_password;
    private LocalDateTime change_time_gender;
    private LocalDateTime change_time_address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
