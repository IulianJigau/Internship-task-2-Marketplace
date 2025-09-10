package com.java.test.junior.mapper;

import com.java.test.junior.model.Product.ProductReview;

import java.util.List;

public interface ProductReviewMapper {

    boolean exists(Long productId, Long userId);

    ProductReview getProductReview(Long productId, Long userId);

    List<ProductReview> getPage(Long userId, Long productId, Integer page, Integer size, Boolean isLiked);

    Long getTotalEntries(Long userId, Long productId, Boolean isLiked);

    void insert(Long productId, Long userId, Boolean isLiked);

    int update(Long productId, Long userId, Boolean isLiked);

    int delete(Long productId, Long userId);
}
