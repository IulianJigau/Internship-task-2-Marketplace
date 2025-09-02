package com.java.test.junior.service.ProductReview;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.RequestResponses.PaginationResponse;

public interface ProductReviewService {

    PaginationResponse<ProductReview> getReviewsPage(Long userId, Long productId, Integer page, Integer size, Boolean isLiked);

    void addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails);
}
