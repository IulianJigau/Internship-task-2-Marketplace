package com.java.test.junior.controller;

import com.java.test.junior.model.Product;
import com.java.test.junior.model.ProductDTO;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.service.ProductReviewService;
import com.java.test.junior.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @NotNull @Positive @PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProductPage(
            @NotNull @Positive @RequestParam(required = true) Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getProductPage(page, page_size, query);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> createProduct(
            @Valid @RequestBody ProductDTO product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> updateProduct(
            @NotNull @Positive @PathVariable Long id,
            @Valid @RequestBody ProductDTO product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(
            @NotNull @Positive @PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ProductReview>> getReviews(
            @NotNull @Positive @PathVariable Long id,
            @RequestParam(required = false) Boolean positive) {
        return productReviewService.getReviewByProductId(id, positive);
    }

    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addReview(
            @NotNull @Positive @PathVariable Long id,
            @NotNull @RequestBody Boolean positive) {
        return productReviewService.addReview(id, positive);
    }

    @DeleteMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteReview(
            @NotNull @Positive @PathVariable Long id,
            @NotNull @Positive @RequestBody Long user_id) {
        return productReviewService.deleteReview(id, user_id);
    }
}
