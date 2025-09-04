package com.java.test.junior.service.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponse.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private final WebClient webClient = WebClient.builder().build();
    private final ProductMapper productMapper;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${junior.storage.site-url}")
    private String storageUrl;
    @Value("${junior.storage.path}")
    private String tempStoragePath;

    @Override
    public void loadProducts(ExtendedUserDetails userDetails) {
        String url = storageUrl + "data/products";

        String csvContent = webClient.get()
                .uri(url)
                .accept(MediaType.TEXT_PLAIN)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    try {
                                        ErrorResponse err = objectMapper.readValue(errorBody, ErrorResponse.class);
                                        return Mono.error(new ResourceNotFoundException(err.getDetails()));
                                    } catch (Exception e) {
                                        return Mono.error(new ResourceNotFoundException("Failed to parse error: " + errorBody));
                                    }
                                });
                    }
                })
                .block();

        if (csvContent == null || csvContent.isBlank()) {
            throw new ResourceNotFoundException("Downloaded CSV content is empty");
        }

        Path destination = Path.of(tempStoragePath, "products.csv");

        try {
            Files.writeString(destination, csvContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ResourceConflictException("Failed to write the CSV content");
        }
        productMapper.bulkImport(destination.toString(), userDetails.getId());
    }
}
