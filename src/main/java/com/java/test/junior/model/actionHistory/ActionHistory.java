package com.java.test.junior.model.actionHistory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActionHistory {
    private Long id;
    private Long userId;
    private String action;
    private String address;
    private LocalDateTime createdAt;
}
