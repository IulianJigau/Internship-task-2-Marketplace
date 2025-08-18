package com.java.test.junior.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String email;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

