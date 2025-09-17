package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.Role;
import com.java.test.junior.model.response.PaginationResponse;
import com.java.test.junior.model.user.User;
import com.java.test.junior.model.user.UserDTO;
import com.java.test.junior.service.product.ProductService;
import com.java.test.junior.service.product.review.ProductReviewService;
import com.java.test.junior.service.role.RoleService;
import com.java.test.junior.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    public User getUserById(@NotNull @Positive @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get users page")
    @GetMapping
    public PaginationResponse<?> getUsersPage(@Valid @ModelAttribute PaginationOptionsDTO paginationOptions) {
        return userService.getUsersPage(paginationOptions, false);
    }

    @Operation(summary = "Get users page")
    @GetMapping("/{userId}/products")
    public PaginationResponse<?> getUserProductsPage(@Valid @ModelAttribute PaginationOptionsDTO paginationOptions,
                                                     @RequestParam(required = false) String query,
                                                     @NotNull @Positive @PathVariable Long userId) {
        return productService.getProductsPage(paginationOptions, query, userId, false);
    }

    @Operation(summary = "Get deleted users' page")
    @GetMapping("/deleted")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public PaginationResponse<?> getDeletedUsersPage(@Valid @ModelAttribute PaginationOptionsDTO paginationOptions) {
        return userService.getUsersPage(paginationOptions, true);
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
    public void updateUser(@NotNull @Positive @PathVariable Long userId,
                           @Size(min = 3, max = 30) @RequestParam(required = false) String username,
                           @Size(min = 5, max = 30) @RequestBody(required = false) @Pattern(regexp = "\\S+") String password,
                           @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.updateUser(userId, username, password, userDetails);
    }

    @Operation(summary = "Delete an user")
    @DeleteMapping("/{userId}")
    public void deleteUser(@NotNull @Positive @PathVariable Long userId,
                           @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.deleteUser(userId, userDetails);
    }

    @Operation(summary = "Clear deleted users")
    @DeleteMapping("/deleted")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public void clearDeletedUsers() {
        userService.clearDeletedUsers();
    }

    @Operation(summary = "Get an user's roles")
    @GetMapping("/{userId}/roles")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public List<Role> getUserRoles(@NotNull @Positive @PathVariable Long userId) {
        return roleService.getUserRoles(userId);
    }

    @Operation(summary = "Assign a role")
    @PostMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public void assignUserRole(@NotNull @Positive @PathVariable Long userId,
                               @NotNull @Positive @RequestParam(required = true) Integer roleId) {
        roleService.assignUserRole(userId, roleId);
    }

    @Operation(summary = "Remove a role")
    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public void removeUserRole(@NotNull @Positive @PathVariable Long userId,
                               @NotNull @Positive @RequestParam(required = true) Integer roleId) {
        roleService.removeUserRole(userId, roleId);
    }

    @Operation(summary = "Get an user's reviews")
    @GetMapping("/{userId}/reviews")
    public PaginationResponse<?> getReviews(@NotNull @Positive @PathVariable Long userId,
                                            @Valid @ModelAttribute PaginationOptionsDTO paginationOptions) {
        return productReviewService.getReviewsPage(userId, null, paginationOptions, null);
    }
}