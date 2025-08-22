package com.java.test.junior.service.ProductReview;

import com.java.test.junior.model.ExtendedUserDetails;
import org.springframework.http.ResponseEntity;

public interface ProductReviewService {
    ResponseEntity<?> getReviewsByUserId(Long userId, Integer page, Integer size);

    ResponseEntity<?> getReviewsByProductId(Long productId, Integer page, Integer size, Boolean positive);

    ResponseEntity<?> addReview(Long productId, Boolean positive, ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteReview(Long productId, Long userId, ExtendedUserDetails userDetails);
}
