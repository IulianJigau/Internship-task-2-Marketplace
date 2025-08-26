package com.java.test.junior.mapper;

import com.java.test.junior.model.ProductReview;

import java.util.List;

public interface ProductReviewMapper {

    boolean exists(Long productId, Long userId);

    ProductReview getProductReview(Long productId, Long userId);

    List<ProductReview> getPageByUserId(Long userId, Integer page, Integer size);

    List<ProductReview> getPageByProductId(Long productId, Integer page, Integer size, Boolean isLiked);

    void insert(Long productId, Long userId, Boolean isLiked);

    int update(Long productId, Long userId, Boolean isLiked);

    int delete(Long productId, Long userId);
}
