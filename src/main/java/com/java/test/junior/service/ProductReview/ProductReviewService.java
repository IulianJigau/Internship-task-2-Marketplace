package com.java.test.junior.service.ProductReview;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductReviewService {
    ResponseEntity<List<ProductReview>> getReviewByUserId(Long userId);

    ResponseEntity<List<ProductReview>> getReviewByProductId(Long productId, Boolean positive);

    ResponseEntity<String> addReview(Long productId, Boolean positive, ExtendedUserDetails userDetails);

    ResponseEntity<String> deleteReview(Long productId, Long userId, ExtendedUserDetails userDetails);
}
