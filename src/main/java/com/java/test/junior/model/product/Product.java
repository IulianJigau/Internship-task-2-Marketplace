package com.java.test.junior.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Boolean isDeleted;
}
