package com.java.test.junior.service.ProductReview;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.mapper.ProductReviewMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
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
    private final UserMapper userMapper;
    private final ProductReviewMapper productReviewMapper;

    private boolean isAdmin(ExtendedUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public ResponseEntity<?> getReviewsPageByUserId(Long userId, Integer page, Integer size) {
        try {
            boolean exists = userMapper.exists(userId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<ProductReview> reviews = productReviewMapper.getPageByUserId(userId, page, size);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getReviewsPageByProductId(Long productId, Integer page, Integer size, Boolean isLiked) {
        try {
            boolean exists = productMapper.exists(productId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<ProductReview> reviews = productReviewMapper.getPageByProductId(productId, page, size, isLiked);
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProductReview productReview = productReviewMapper.getProductReview(productId, userDetails.getId());
            if (productReview != null) {
                if(productReview.getIsLiked() == isLiked) {
                    productReviewMapper.delete(productId, userDetails.getId());
                }
                else{
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

    @Override
    public ResponseEntity<?> deleteReview(Long productId, Long userId, ExtendedUserDetails userDetails) {
        try {
            if (!isAdmin(userDetails) && !userDetails.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            return productReviewMapper.delete(productId, userId) > 0 ?
                    ResponseEntity.ok().build() :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
