package com.java.test.junior.service.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponse.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private final WebClient webClient = WebClient.builder().build();
    private final ProductMapper productMapper;
    private final String storageUrl = "http://localhost:8082/dat/stream/products.csv";
    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${junior.storage.path}")
    private String tempStoragePath;

    @Override
    public Mono<Void> load(ExtendedUserDetails userDetails) {
        Path destination = Path.of(tempStoragePath, "products.csv");

        return Mono.fromRunnable(() -> {
                    try {
                        Files.createDirectories(destination.getParent());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .then(
                        webClient.get()
                                .uri(storageUrl)
                                .accept(MediaType.TEXT_PLAIN)
                                .exchangeToMono(response -> {
                                    HttpStatusCode status = response.statusCode();

                                    if (status.isError()) {
                                        return response.bodyToMono(String.class).flatMap(errorBody -> {
                                            try {
                                                ErrorResponse err = objectMapper.readValue(errorBody, ErrorResponse.class);
                                                return Mono.error(new ResourceNotFoundException(err.getDetails()));
                                            } catch (Exception e) {
                                                return Mono.error(new ResourceNotFoundException("Failed to parse error: " + errorBody));
                                            }
                                        });
                                    }

                                    return response.bodyToFlux(DataBuffer.class)
                                            .transform(bufFlux -> DataBufferUtils.write(bufFlux, destination,
                                                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))
                                            .then();
                                })
                )
                .then(Mono.fromRunnable(() -> {
                    try {
                        if (!Files.exists(destination) || Files.size(destination) == 0) {
                            throw new ResourceNotFoundException("Downloaded CSV file is empty or missing");
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }))
                .then(Mono.fromRunnable(() ->
                        productMapper.bulkImport(destination.toString(), userDetails.getId())
                ).subscribeOn(Schedulers.boundedElastic()))
                .then();
    }
}
