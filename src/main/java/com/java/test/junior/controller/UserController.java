package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import com.java.test.junior.service.ProductReview.ProductReviewService;
import com.java.test.junior.service.RoleService.RoleService;
import com.java.test.junior.service.UserService.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ProductReviewService productReviewService;

    @GetMapping("/self")
        public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return userService.getUserById(userDetails.getId());
    }

    @GetMapping("/{userId}")
        public ResponseEntity<User> getUserById(
            @NotNull @Positive @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
        public ResponseEntity<List<User>> getUserPage(
            @NotNull @Positive @RequestParam(required = true) Integer page,
            @Max(1000) @Positive @RequestParam(defaultValue = "10") Integer page_size) {
        return userService.getUserPage(page, page_size);
    }

    @PostMapping
    public ResponseEntity<String> createUser(
            @Valid @RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
        public ResponseEntity<String> updateUser(
            @NotNull @Positive @PathVariable Long userId,
            @Valid @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return  userService.updateUser(userId, userDTO, userDetails);
    }

    @DeleteMapping("/{userId}")
        public ResponseEntity<String> deleteUser(
            @NotNull @Positive @PathVariable Long userId,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return userService.deleteUser(userId,  userDetails);
    }


    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getUserRoles(
            @NotNull @Positive @PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addUserRole(
            @NotNull @Positive @PathVariable Long userId,
            @NotEmpty @RequestParam(required = true) String role) {
        return  roleService.addUserRole(userId, role);
    }

    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeUserRole(
            @NotNull @Positive @PathVariable Long userId,
            @NotEmpty @RequestParam(required = true) String role) {
        return  roleService.removeUserRole(userId, role);
    }

    @GetMapping("/{userId}/reviews")
        public ResponseEntity<List<ProductReview>> getReviews(
            @NotNull @Positive @PathVariable Long userId) {
        return productReviewService.getReviewByUserId(userId);
    }
}
