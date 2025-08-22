package com.java.test.junior.controller;

import com.java.test.junior.service.RoleService.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Role Handler", description = "Performs role oriented operations")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "Get roles",
            description = "Retrieves a list of the available roles"
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRoles() {
        return roleService.getRoles();
    }

    @Operation(
            summary = "Create role",
            description = "Creates a role based on the name provided"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRole(
            @NotBlank @Size(min = 3, max = 30) @RequestParam String name) {
        return roleService.createRole(name);
    }

    @Operation(
            summary = "Delete role",
            description = "Deletes a role based on the name provided"
    )
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRole(
            @NotNull @Positive @PathVariable Integer roleId) {
        return roleService.deleteRole(roleId);
    }
}