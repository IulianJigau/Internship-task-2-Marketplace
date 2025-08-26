package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.service.ProductReview.ProductReviewService;
import com.java.test.junior.service.ProductService.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Handler", description = "Performs product oriented operations")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @Operation(summary = "Get product by id")
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(
            @NotNull @Positive @PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @Operation(summary = "Get products page")
    @GetMapping
    public ResponseEntity<?> getProductsPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getProductsPage(page, page_size, query);
    }

    @Operation(summary = "Get the deleted products' page")
    @GetMapping("/deleted")
    public ResponseEntity<?> getDeletedProductsPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getDeletedProductsPage(page, page_size, query);
    }

    @Operation(summary = "Clear deleted products")
    @DeleteMapping("/deleted/clear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> clearDeletedProducts() {
        return productService.clearDeletedProducts();
    }

    @Operation(summary = "Create a product")
    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.createProduct(product, userDetails);
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @NotNull @Positive @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.updateProduct(productId, product, userDetails);
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @NotNull @Positive @PathVariable Long productId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.deleteProduct(productId, userDetails);
    }

    @Operation(summary = "Get product reviews page")
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<?> getReviewsPage(
            @NotNull @Positive @PathVariable Long productId,
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) Boolean isLiked) {
        return productReviewService.getReviewsPageByProductId(productId, page, page_size, isLiked);
    }

    @Operation(summary = "Add, update or remove a product review")
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> addReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @RequestParam Boolean isLiked,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productReviewService.addReview(productId, isLiked, userDetails);
    }

    @Operation(summary = "Remove a product review")
    @DeleteMapping("/{productId}/reviews")
    public ResponseEntity<?> deleteReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @Positive @RequestParam Long userId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productReviewService.deleteReview(productId, userId, userDetails);
    }
}
