package com.java.test.junior.service.ProductReview;

import com.java.test.junior.model.ExtendedUserDetails;
import org.springframework.http.ResponseEntity;

public interface ProductReviewService {
    ResponseEntity<?> getReviewsPageByUserId(Long userId, Integer page, Integer size);

    ResponseEntity<?> getReviewsPageByProductId(Long productId, Integer page, Integer size, Boolean isLiked);

    ResponseEntity<?> addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteReview(Long productId, Long userId, ExtendedUserDetails userDetails);
}
