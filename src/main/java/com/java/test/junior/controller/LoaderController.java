package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.service.LoaderService.LoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Data Loader", description = "Loads data from pre-made csv files")
@RestController
@RequestMapping("/loading")
@RequiredArgsConstructor
public class LoaderController {

    private final LoaderService loaderService;

    @Operation(
            summary = "Load products",
            description = "Adds products from the provided csv file and appends them to the current admin"
    )
    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> loadProducts(
            @AuthenticationPrincipal ExtendedUserDetails userDetails
    ) {
        return loaderService.loadProducts(userDetails);
    }
}
