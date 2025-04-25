package com.example.BunBlog.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.example.BunBlog.model.enums.GenderEnum;
import com.example.BunBlog.security.RefreshToken;
import com.example.BunBlog.websocket.ChatMessage;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
@Entity
public class User {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Tên không được trống")
    private String username;
    
    @NotBlank(message = "Email không được trống")
    @Email(message = "Phải điền đúng kiểu email")
    private String email;

    @NotBlank(message = "Mật khẩu không được trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    private int age;

    @Column(length = 1000)
    private String avatar;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshToken; 

    @OneToOne(mappedBy = "user")
    private UserPage userPage;

    @OneToOne(mappedBy = "user")
    private UserHistory userHistory;

    @OneToMany(mappedBy = "user")
    private List<Post> post;
    
    @OneToOne(mappedBy = "user")
    private PostLike postLike;
}