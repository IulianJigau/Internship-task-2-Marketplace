package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.service.ProductReview.ProductReviewService;
import com.java.test.junior.service.ProductService.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @GetMapping("/{productId}")
        public ResponseEntity<?> getProductById(
            @NotNull @Positive @PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping
        public ResponseEntity<?> getProductPage(
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getProductPage(page, page_size, query);
    }

    @PostMapping
        public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.createProduct(product, userDetails);
    }

    @PutMapping("/{productId}")
        public ResponseEntity<?> updateProduct(
            @NotNull @Positive @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.updateProduct(productId, product, userDetails);
    }

    @DeleteMapping("/{productId}")
        public ResponseEntity<?> deleteProduct(
            @NotNull @Positive @PathVariable Long productId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.deleteProduct(productId, userDetails);
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<?> getReviews(
            @NotNull @Positive @PathVariable Long productId,
            @RequestParam(required = false) Boolean positive) {
        return productReviewService.getReviewByProductId(productId, positive);
    }

    @PostMapping("/{productId}/reviews")
        public ResponseEntity<?> addReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @RequestParam Boolean positive,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productReviewService.addReview(productId, positive, userDetails);
    }

    @DeleteMapping("/{productId}/reviews")
        public ResponseEntity<?> deleteReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @Positive @RequestBody Long user_id,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productReviewService.deleteReview(productId, user_id, userDetails);
    }
}
