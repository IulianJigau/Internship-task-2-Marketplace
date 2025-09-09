package com.java.test.junior.service.loader;

import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponse.ErrorResponse;
import com.java.test.junior.model.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaderServiceImp implements LoaderService {

    private static final int BATCH_SIZE = 50;
    private final WebClient webClient = WebClient.create();
    private final ProductMapper productMapper;

    @Qualifier("storageServers")
    private final List<Resource> storageServers;

    @Override
    public List<Resource> getResources() {
        return storageServers;
    }

    @Override
    public List<String> getResourceFiles(Integer resourceId) {
        boolean exists = storageServers.stream()
                .anyMatch(server -> server.getId().equals(resourceId));

        if (!exists) {
            throw new ResourceNotFoundException("The requested resource was not found.");
        }

        String baseUrl = storageServers.get(resourceId).getPath();

        return webClient.get()
                .uri(baseUrl + "/data/files")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();
    }

    @Override
    public Mono<ResponseEntity<Object>> load(String fileName, ExtendedUserDetails userDetails) {
        return webClient.get()
                .uri("http://localhost:8082/data/stream/" + fileName)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })
                .publishOn(Schedulers.boundedElastic())
                .<String>handle((event, sink) -> {
                    if ("error".equals(event.event())) {
                        sink.error(new ResourceNotFoundException(event.data()));
                    } else if ("data".equals(event.event())) {
                        sink.next(event.data());
                    }
                })
                .skip(1)
                .buffer(BATCH_SIZE)
                .concatMap(batch -> processBatch(batch, userDetails))
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()))
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorResponse(ex.getMessage())))
                );
    }

    private Mono<Void> processBatch(List<String> batch, ExtendedUserDetails userDetails) {
        return Mono.fromCallable(() -> {
            Path batchPath = Files.createTempFile(Paths.get("C:/Temp"), "batch-", ".csv");
            try (BufferedWriter writer = Files.newBufferedWriter(batchPath, StandardCharsets.UTF_8)) {
                for (String line : batch) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            productMapper.bulkImport(batchPath.toString(), userDetails.getId());
            Files.deleteIfExists(batchPath);
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}

