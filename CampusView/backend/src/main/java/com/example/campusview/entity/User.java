package com.example.campusview.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String email;

    @Column(nullable = false)
    private String role = "USER";  // USER, ADMIN

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastLogin;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
} 