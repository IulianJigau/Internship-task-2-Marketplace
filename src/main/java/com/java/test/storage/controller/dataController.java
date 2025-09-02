package com.java.test.storage.controller;

import com.java.test.storage.service.DataRetriever.DataRetriever;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Data Transmitter", description = "Provides data for the core application")
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class dataController {

    private final DataRetriever dataRetriever;

    @Operation(summary = "Load products from csv")
    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public String provideProducts() {
        return dataRetriever.provideProducts();
    }
}
