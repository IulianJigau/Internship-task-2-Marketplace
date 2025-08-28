package com.java.test.junior.service.ProductReview;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.mapper.ProductReviewMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.RequestResponses.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImp implements ProductReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ProductReviewServiceImp.class);

    private final ProductMapper productMapper;
    private final ProductReviewMapper productReviewMapper;

    @Override
    public ResponseEntity<?> getReviewsPage(Long userId, Long productId, Integer page, Integer size, Boolean isLiked) {
        try {
            boolean exists = productMapper.exists(productId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse("The requested product was not found.")
                );
            }

            List<ProductReview> reviews = productReviewMapper.getPage(userId, productId, page, size, isLiked);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> addReview(Long productId, Boolean isLiked, ExtendedUserDetails userDetails) {
        try {
            boolean exists = productMapper.exists(productId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse("The requested product was not found.")
                );
            }

            ProductReview productReview = productReviewMapper.getProductReview(productId, userDetails.getId());
            if (productReview != null) {
                if (productReview.getIsLiked() == isLiked) {
                    productReviewMapper.delete(productId, userDetails.getId());
                } else {
                    productReviewMapper.update(productId, userDetails.getId(), isLiked);
                }
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                productReviewMapper.insert(productId, userDetails.getId(), isLiked);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
