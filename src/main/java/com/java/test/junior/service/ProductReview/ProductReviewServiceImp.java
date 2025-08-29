package com.java.test.junior.service.ProductReview;

import com.java.test.junior.exception.DatabaseFailException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.mapper.ProductReviewMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImp implements ProductReviewService {

    private final ProductMapper productMapper;
    private final ProductReviewMapper productReviewMapper;

    @Override
    public PaginationResponse<ProductReview> getReviewsPage(Long userId, Long productId, Integer page, Integer size, Boolean isLiked) {
        try {
            List<ProductReview> reviews = productReviewMapper.getPage(userId, productId, page, size, isLiked);
            Long entries = productReviewMapper.getTotalEntries(userId, productId, isLiked);
            return new PaginationResponse<>(entries, reviews);
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }

    @Override
    public void addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails) {
        try {
            boolean exists = productMapper.exists(productId);
            if (!exists) {
                throw new ResourceNotFoundException("The requested product was not found.");
            }

            ProductReview productReview = productReviewMapper.getProductReview(productId, userDetails.getId());
            if (productReview != null) {
                if (productReview.getIsLiked() == isLiked) {
                    productReviewMapper.delete(productId, userDetails.getId());
                } else {
                    productReviewMapper.update(productId, userDetails.getId(), isLiked);
                }
            } else {
                productReviewMapper.insert(productId, userDetails.getId(), isLiked);
            }
        } catch (DataAccessException e) {
            throw new DatabaseFailException(e.getMessage());
        }
    }
}
