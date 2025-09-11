package com.java.test.junior.model.product;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductReview {
    private Long productId;
    private Long userId;
    private Boolean isLiked;
    private LocalDateTime createdAt;
}
