package com.java.test.junior.controller;

import com.java.test.junior.model.ProductReview;
import com.java.test.junior.model.User;
import com.java.test.junior.model.UserDTO;
import com.java.test.junior.service.ProductReviewService;
import com.java.test.junior.service.RoleService;
import com.java.test.junior.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;
    private final ProductReviewService productReviewService;

    @GetMapping("/self")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @NotNull @Positive @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/find")
    public ResponseEntity<User> getUserByEmail(
            @NotBlank @Email @RequestParam(required = true) String email) {
        return userService.getUserByEmail(email);
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

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUser(
            @NotNull @Positive @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO){
        return  userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteUser(
            @NotNull @Positive @PathVariable Long id) {
        return userService.deleteUser(id);
    }


    @GetMapping("/{id}/roles")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<List<String>> getUserRoles(
            @NotNull @Positive @PathVariable Long id) {
        return roleService.getUserRoles(id);
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> addUserRoles(
            @NotNull @Positive @PathVariable Long id,
            @NotEmpty @RequestBody List<String> roles) {
        return  roleService.addUserRoles(id, roles);
    }

    @DeleteMapping("/{id}/roles")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    public ResponseEntity<String> removeUserRoles(
            @NotNull @Positive @PathVariable Long id,
            @NotEmpty @RequestBody List<String> roles) {
        return  roleService.removeUserRoles(id, roles);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ProductReview>> getReviews(
            @NotNull @Positive @PathVariable Long id) {
        return productReviewService.getReviewByUserId(id);
    }
}
