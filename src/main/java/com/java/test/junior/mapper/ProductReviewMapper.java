package com.java.test.junior.mapper;

import com.java.test.junior.model.ProductReview;

import java.util.List;

public interface ProductReviewMapper {

    boolean exists(Long productId, Long userId);

    List<ProductReview> findByUserId(Long userId, Integer page, Integer size);

    List<ProductReview> findByProductId(Long productId, Integer page, Integer size, Boolean positive);

    void insert(Long productId, Long userId, Boolean positive);

    int update(Long productId, Long userId, Boolean positive);

    int delete(Long productId, Long userId);
}
