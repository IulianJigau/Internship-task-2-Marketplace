package com.java.test.junior.service.productReview;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface ProductReviewService {

    PaginationResponse<?> getReviewsPage(Long userId, Long productId, PaginationOptionsDTO paginationOptions, Boolean isLiked);

    void addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails);
}
