package com.java.test.junior.model.actionHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionHistoryDTO {
    private Long userId;
    private String action;
    private String address;
}
