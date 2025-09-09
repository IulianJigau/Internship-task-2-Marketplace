package com.java.test.junior.service.loader;

import com.java.test.junior.mapper.ProductMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @Override
    public Mono<Void> load(String fileName, ExtendedUserDetails userDetails) {
        return webClient.get()
                .uri("http://localhost:8082/data/stream/" + fileName)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .publishOn(Schedulers.boundedElastic())
                .skip(1)
                .buffer(BATCH_SIZE)
                .concatMap(batch -> processBatch(batch, userDetails))
                .then();
    }

    private Mono<Object> processBatch(List<String> batch, ExtendedUserDetails userDetails) {
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
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
