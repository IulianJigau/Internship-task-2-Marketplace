package com.java.test.junior.service.product.review;

import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductReviewMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.product.ProductReview;
import com.java.test.junior.model.response.PaginationResponse;
import com.java.test.junior.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductService productService;
    private final ProductReviewMapper productReviewMapper;

    @Override
    public PaginationResponse<ProductReview> getReviewsPage(Long userId, Long productId, PaginationOptionsDTO paginationOptions, Boolean isLiked) {
        List<ProductReview> reviews = productReviewMapper.getPage(userId, productId, paginationOptions.getPage(), paginationOptions.getPageSize(), isLiked);

        long entries = -1L;
        if (paginationOptions.getRefresh()) {
            entries = productReviewMapper.getTotalEntries(userId, productId, isLiked);
        }

        return new PaginationResponse<>(entries, reviews);
    }

    @Override
    public void addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails) {
        if (!productService.existsProductId(productId)) {
            throw new ResourceNotFoundException("The requested product was not found.");
        }

        ProductReview productReview = productReviewMapper.getProductReview(productId, userDetails.getId());
        if(productReview == null){
            productReviewMapper.insert(productId, userDetails.getId(), isLiked);
            return;
        }

        if (productReview.getIsLiked() == isLiked) {
            productReviewMapper.delete(productId, userDetails.getId());
            return;
        }

        productReviewMapper.update(productId, userDetails.getId(), isLiked);
    }
}
