package com.java.test.junior.controller;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.service.loader.LoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Data Loader", description = "Loads data from pre-made csv files")
@RestController
@Validated
@RequestMapping("/loading")
@RequiredArgsConstructor
public class LoaderController {

    private final LoaderService loaderService;

    @Operation(summary = "Load products from csv")
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public void loadProducts(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        loaderService.load(userDetails).subscribe();
    }
}
