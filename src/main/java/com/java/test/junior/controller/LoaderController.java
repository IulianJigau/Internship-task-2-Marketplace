package com.java.test.junior.controller;

import com.java.test.junior.client.model.Provider;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.service.loader.LoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Data Loader", description = "Loads data from pre-made csv files")
@RestController
@Validated
@RequestMapping("/loader")
@RequiredArgsConstructor
public class LoaderController {

    private final LoaderService loaderService;

    @Operation(summary = "Load products from csv")
    @PostMapping("/products/{resourceId}/{fileName}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public void loadProducts(@NotNull @PathVariable Integer resourceId,
                             @NotBlank @PathVariable String fileName,
                             @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        loaderService.loadProducts(resourceId, fileName, userDetails);
    }

    @Operation(summary = "Get the available providers' addresses")
    @GetMapping("/providers")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public List<Provider> getProviders() {
        return loaderService.getProviders();
    }

    @Operation(summary = "List a provider's available files")
    @GetMapping("/providers/{resourceId}")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public List<String> getProviderFiles(@NotNull @PathVariable Integer resourceId) {
        return loaderService.getProviderFiles(resourceId);
    }
}
