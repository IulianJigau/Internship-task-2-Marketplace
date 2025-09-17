package com.java.test.junior.model.actionHistory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActionHistory {
    @JsonIgnore
    private Long id;
    private Long userId;
    private String action;
    private String address;
    private LocalDateTime createdAt;
}
