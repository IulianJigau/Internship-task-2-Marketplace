package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.UserDTO;
import com.java.test.junior.service.ProductReview.ProductReviewService;
import com.java.test.junior.service.ProductService.ProductService;
import com.java.test.junior.service.RoleService.RoleService;
import com.java.test.junior.service.UserService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Handler", description = "Performs user oriented operations")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @Operation(summary = "Get the current user")
    @GetMapping("/self")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return userService.getUserById(userDetails.getId());
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(
            @NotNull @Positive @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get users page")
    @GetMapping
    public ResponseEntity<?> getUsersPage(
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return userService.getUsersPage(page, page_size);
    }

    @Operation(summary = "Get deleted users page")
    @GetMapping("/deleted")
    public ResponseEntity<?> getDeletedUsersPage(
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return userService.getDeletedUsersPage(page, page_size);
    }

    @Operation(summary = "Clear deleted users")
    @DeleteMapping("/deleted/clear")
    public ResponseEntity<?> clearDeletedUsers() {
        return userService.clearDeletedUsers();
    }

    @Operation(summary = "Get users page")
    @GetMapping("/{userId}/products")
    public ResponseEntity<?> getUserProductsPage(
            @NotNull @Positive @RequestParam Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size,
            @RequestParam(required = false) String query,
            @NotNull @Positive @PathVariable Long userId) {
        return productService.getProductsPageByUserId(page, page_size, query, userId);
    }

    @Operation(summary = "Create an user")
    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Update user's credentials")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @NotNull @Positive @PathVariable Long userId,
            @Size(min = 3, max = 30) @RequestParam(required = false) String username,
            @Size(min = 5, max = 30) @RequestBody(required = false) @Pattern(regexp = "\\S+") String password,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        ResponseEntity<?> response = ResponseEntity.badRequest().build();
        if (username != null) {
            response = userService.updateUsername(userId, username, userDetails);
        }
        if (password != null) {
            response = userService.updatePassword(userId, password, userDetails);
        }
        return response;
    }

    @Operation(summary = "Delete an user")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @NotNull @Positive @PathVariable Long userId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return userService.deleteUser(userId, userDetails);
    }

    @Operation(summary = "Get an user's roles")
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserRoles(
            @NotNull @Positive @PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    @Operation(summary = "Append a role")
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUserRole(
            @NotNull @Positive @PathVariable Long userId,
            @NotNull @Positive @RequestParam(required = true) Integer roleId) {
        return roleService.addUserRole(userId, roleId);
    }

    @Operation(summary = "Remove a role")
    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserRole(
            @NotNull @Positive @PathVariable Long userId,
            @NotNull @Positive @RequestParam(required = true) Integer roleId) {
        return roleService.removeUserRole(userId, roleId);
    }

    @Operation(summary = "Get an user's reviews")
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<?> getReviews(
            @NotNull @Positive @PathVariable Long userId,
            @NotNull @Positive @RequestParam(required = true) Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return productReviewService.getReviewsPageByUserId(userId, page, page_size);
    }
}
