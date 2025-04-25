package com.example.BunBlog.websocket;

import java.util.Optional;

import com.example.BunBlog.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name="ChatMessages")
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT") // Chỉ định lưu dưới dạng TEXT
    private String messageContent;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender; //sender

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver; //receiver
}
