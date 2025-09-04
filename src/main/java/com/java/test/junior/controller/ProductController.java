package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.service.product.ProductService;
import com.java.test.junior.service.productReview.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Handler", description = "Performs product oriented operations")
@RestController
@Validated
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @Operation(summary = "Get product by id")
    @GetMapping("/{productId}")
    public Product getProductById(
            @NotNull @Positive @PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @Operation(summary = "Get products page")
    @GetMapping
    public PaginationResponse<?> getProductsPage(
            @Valid @ModelAttribute PaginationOptionsDTO paginationOptions,
            @RequestParam(required = false) String query) {
        return productService.getProductsPage(paginationOptions, query, null, false);
    }

    @Operation(summary = "Get the deleted products' page")
    @GetMapping("/deleted")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public PaginationResponse<?> getDeletedProductsPage(
            @Valid @ModelAttribute PaginationOptionsDTO paginationOptions,
            @RequestParam(required = false) String query) {
        return productService.getProductsPage(paginationOptions, query, null, true);
    }

    @Operation(summary = "Create a product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.createProduct(product, userDetails);
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{productId}")
    public void updateProduct(
            @NotNull @Positive @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        productService.updateProduct(productId, product, userDetails);
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{productId}")
    public void deleteProduct(
            @NotNull @Positive @PathVariable Long productId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        productService.deleteProduct(productId, userDetails);
    }

    @Operation(summary = "Clear deleted products")
    @DeleteMapping("/deleted")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public void clearDeletedProducts() {
        productService.clearDeletedProducts();
    }


    @Operation(summary = "Get product reviews page")
    @GetMapping("/{productId}/reviews")
    public PaginationResponse<?> getReviewsPage(
            @NotNull @Positive @PathVariable Long productId,
            @Valid @ModelAttribute PaginationOptionsDTO paginationOptions,
            @RequestParam(required = false) Boolean isLiked) {
        return productReviewService.getReviewsPage(null, productId, paginationOptions, isLiked);
    }

    @Operation(summary = "Add, update or remove a product review")
    @PostMapping("/{productId}/reviews")
    public void addReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @RequestParam Boolean isLiked,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        productReviewService.addReview(productId, isLiked, userDetails);
    }
}
