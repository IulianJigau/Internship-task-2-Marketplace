package com.java.test.junior.controller;

import com.java.test.junior.config.GlobalExceptionHandler;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;
import com.java.test.junior.service.loader.LoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Data Loader", description = "Loads data from pre-made csv files")
@RestController
@Validated
@RequestMapping("/loading")
@RequiredArgsConstructor
public class LoaderController {

    private final LoaderService loaderService;

    @Operation(summary = "Load products from csv")
    @PostMapping("/products/{resourceId}/{fileName}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public Mono<ResponseEntity<Object>> loadProducts(
            @NotNull @PathVariable Integer resourceId,
            @NotBlank @PathVariable String fileName,
            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        return loaderService.loadProducts(resourceId, fileName, userDetails);
    }

    @GetMapping("/resources")
    public List<Resource> resources() {
        return loaderService.getResources();
    }

    @GetMapping("/resources/{resourceId}")
    public List<String> resourcesData(
            @NotNull @PathVariable Integer resourceId
    ) {
       return loaderService.getResourceFiles(resourceId);
    }
}
