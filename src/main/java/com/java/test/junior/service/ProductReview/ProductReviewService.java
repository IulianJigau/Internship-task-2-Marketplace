package com.java.test.junior.service.ProductReview;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductReviewService {
    ResponseEntity<?> getReviewByUserId(Long userId);

    ResponseEntity<?> getReviewByProductId(Long productId, Boolean positive);

    ResponseEntity<?> addReview(Long productId, Boolean positive, ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteReview(Long productId, Long userId, ExtendedUserDetails userDetails);
}
