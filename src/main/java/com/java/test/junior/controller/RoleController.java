package com.java.test.junior.controller;

import com.java.test.junior.model.Role;
import com.java.test.junior.service.RoleService.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role Handler", description = "Performs role oriented operations")
@RestController
@Validated
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get roles")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @Operation(summary = "Create role")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createRole(
            @NotBlank @Size(min = 3, max = 30) @RequestParam String name) {
        roleService.createRole(name);
    }

    @Operation(summary = "Delete role")
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(
            @NotNull @Positive @PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
    }
}