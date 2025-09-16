package com.java.test.junior.model.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductReview {
    private Long productId;
    private Long userId;
    private Boolean isLiked;
    private LocalDateTime createdAt;
}
