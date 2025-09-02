package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Product.Product;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import com.java.test.junior.service.ProductReview.ProductReviewService;
import com.java.test.junior.service.Product.ProductService;
import com.java.test.junior.service.Role.RoleService;
import com.java.test.junior.service.User.UserService;
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

import java.util.List;

@Tag(name = "User Handler", description = "Performs user oriented operations")
@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @Operation(summary = "Get the current user")
    @GetMapping("/self")
    public User getCurrentUser(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return userService.getUserById(userDetails.getId());
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{userId}")
    public User getUserById(
            @NotNull @Positive @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get users page")
    @GetMapping
    public PaginationResponse<User> getUsersPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return userService.getUsersPage(page, page_size, false);
    }

    @Operation(summary = "Get users page")
    @GetMapping("/{userId}/products")
    public PaginationResponse<Product> getUserProductsPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query,
            @NotNull @Positive @PathVariable Long userId) {
        return productService.getProductsPage(page, page_size, query, userId, false);
    }

    @Operation(summary = "Get deleted users' page")
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public PaginationResponse<User> getDeletedUsersPage(
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return userService.getUsersPage(page, page_size, true);
    }

    @Operation(summary = "Create an user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(
            @Valid @RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Update user's credentials")
    @PutMapping("/{userId}")
    public void updateUser(
            @NotNull @Positive @PathVariable Long userId,
            @Size(min = 3, max = 30) @RequestParam(required = false) String username,
            @Size(min = 5, max = 30) @RequestBody(required = false) @Pattern(regexp = "\\S+") String password,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.update(userId, username, password, userDetails);
    }

    @Operation(summary = "Delete an user")
    @DeleteMapping("/{userId}")
    public void deleteUser(
            @NotNull @Positive @PathVariable Long userId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.deleteUser(userId, userDetails);
    }

    @Operation(summary = "Clear deleted users")
    @DeleteMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public void clearDeletedUsers() {
        userService.clearDeletedUsers();
    }

    @Operation(summary = "Get an user's roles")
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> getUserRoles(
            @NotNull @Positive @PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    @Operation(summary = "Assign a role")
    @PostMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void assignUserRole(
            @NotNull @Positive @PathVariable Long userId,
            @NotNull @Positive @RequestParam(required = true) Integer roleId) {
        roleService.assignUserRole(userId, roleId);
    }

    @Operation(summary = "Remove a role")
    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public void removeUserRole(
            @NotNull @Positive @PathVariable Long userId,
            @NotNull @Positive @RequestParam(required = true) Integer roleId) {
        roleService.removeUserRole(userId, roleId);
    }

    @Operation(summary = "Get an user's reviews")
    @GetMapping("/{userId}/reviews")
    public PaginationResponse<ProductReview> getReviews(
            @NotNull @Positive @PathVariable Long userId,
            @Positive @RequestParam(defaultValue = "1") Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return productReviewService.getReviewsPage(userId, null, page, page_size, null);
    }
}
