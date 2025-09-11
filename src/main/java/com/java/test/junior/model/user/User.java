package com.java.test.junior.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Boolean isDeleted;
}

