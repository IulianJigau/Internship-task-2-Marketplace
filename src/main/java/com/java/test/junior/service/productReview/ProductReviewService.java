package com.java.test.junior.service.productReview;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponses.PaginationResponse;

public interface ProductReviewService {

    PaginationResponse<?> getReviewsPage(Long userId, Long productId, Integer page, Integer size, Boolean refresh, Boolean isLiked);

    void addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails);
}
