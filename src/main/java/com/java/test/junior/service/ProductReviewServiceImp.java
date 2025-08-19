package com.java.test.junior.service;

import com.java.test.junior.mapper.ProductReviewMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImp implements ProductReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ProductReviewServiceImp.class);
    private final UserMapper userMapper;
    private final ProductReviewMapper productReviewMapper;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public ResponseEntity<List<ProductReview>> getReviewByUserId(Long userId) {
        try {
            List<ProductReview> reviews = productReviewMapper.findByUserId(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<ProductReview>> getReviewByProductId(Long productId, Boolean positive) {
        try {
            List<ProductReview> reviews = productReviewMapper.findByProductId(productId, positive);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> addReview(Long productId, Boolean positive) {
        Authentication authentication = getAuthentication();
        User user = userMapper.findByEmail(authentication.getName());
        try {
            productReviewMapper.insert(productId, user.getId(), positive);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteReview(Long productId, Long userId) {
        Authentication authentication = getAuthentication();

        User user = userMapper.findById(userId);
        if (user != null) {
            boolean isOwner = user.getEmail().equals(authentication.getName());

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || isOwner) {
                try {
                    productReviewMapper.delete(productId, userId);
                    return ResponseEntity.ok().build();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return ResponseEntity.internalServerError().build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.notFound().build();
    }
}
