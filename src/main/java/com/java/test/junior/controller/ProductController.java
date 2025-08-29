package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.Product.ProductDTO;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.service.ProductReview.ProductReviewService;
import com.java.test.junior.service.ProductService.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @ResponseStatus(HttpStatus.OK)
    public Product getProductById(
            @NotNull @Positive @PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @Operation(summary = "Get products page")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginationResponse<Product> getProductsPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getProductsPage(page, page_size, query, null, false);
    }

    @Operation(summary = "Get the deleted products' page")
    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public PaginationResponse<Product> getDeletedProductsPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getProductsPage(page, page_size, query, null, true);
    }

    @Operation(summary = "Clear deleted products")
    @DeleteMapping("/deleted/clear")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void clearDeletedProducts() {
        productService.clearDeletedProducts();
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
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(
            @NotNull @Positive @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        productService.updateProduct(productId, product, userDetails);
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(
            @NotNull @Positive @PathVariable Long productId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        productService.deleteProduct(productId, userDetails);
    }

    @Operation(summary = "Get product reviews page")
    @GetMapping("/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public PaginationResponse<ProductReview> getReviewsPage(
            @NotNull @Positive @PathVariable Long productId,
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) Boolean isLiked) {
        return productReviewService.getReviewsPage(null, productId, page, page_size, isLiked);
    }

    @Operation(summary = "Add, update or remove a product review")
    @PostMapping("/{productId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public void addReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @RequestParam Boolean isLiked,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        productReviewService.addReview(productId, isLiked, userDetails);
    }
}
