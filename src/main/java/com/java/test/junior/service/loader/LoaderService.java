package com.java.test.junior.service.loader;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoaderService {
    List<Resource> getResources();

    Mono<ResponseEntity<Object>> load(String fileName, ExtendedUserDetails userDetails);

    List<String> getResourceFiles(Integer resourceId);
}
