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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Handler", description = "Performs product oriented operations")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @Operation(
            summary = "Get product by id",
            description = "Retrieves the product based on it's id"
    )
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(
            @NotNull @Positive @PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @Operation(
            summary = "Get products page",
            description = "Retrieves a selection of products based on the page number, page size and the keywords requested"
    )
    @GetMapping
    public ResponseEntity<?> getProductsPage(
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getProductsPage(page, page_size, query);
    }

    @Operation(
            summary = "Get deleted products page",
            description = "Retrieves a selection of the deleted products based on the page number, page size and the keywords requested"
    )
    @GetMapping("/deleted")
    public ResponseEntity<?> getDeletedProductsPage(
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query) {
        return productService.getDeletedProductsPage(page, page_size, query);
    }

    @Operation(
            summary = "Clear deleted products",
            description = "Removes deleted products' entries from the database"
    )
    @DeleteMapping("/deleted/clear")
    public ResponseEntity<?> clearDeletedProducts() {
        return productService.clearDeletedProducts();
    }

    @Operation(
            summary = "Create a product",
            description = "Creates a product and appends it to the current user"
    )
    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.createProduct(product, userDetails);
    }

    @Operation(
            summary = "Update a product",
            description = "Updates a product based on the product object and the id provided"
    )
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @NotNull @Positive @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.updateProduct(productId, product, userDetails);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product based on the id provided"
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @NotNull @Positive @PathVariable Long productId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productService.deleteProduct(productId, userDetails);
    }

    @Operation(
            summary = "Get product reviews page",
            description = "Retrieves a selection of product reviews based on the product's id, page number, page size and the filter provided"
    )
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<?> getReviewsPage(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) Boolean positive) {
        return productReviewService.getReviewsPageByProductId(productId, page, page_size, positive);
    }

    @Operation(
            summary = "Add a product review",
            description = "Adds a review on a product based on the id provided, whether the review is positive or not, and appends it to the current user"
    )
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<?> addReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @RequestParam Boolean positive,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productReviewService.addReview(productId, positive, userDetails);
    }

    @Operation(
            summary = "Remove a product review",
            description = "Deletes a review based on the product id and user id provided"
    )
    @DeleteMapping("/{productId}/reviews")
    public ResponseEntity<?> deleteReview(
            @NotNull @Positive @PathVariable Long productId,
            @NotNull @Positive @RequestParam Long userId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return productReviewService.deleteReview(productId, userId, userDetails);
    }
}
