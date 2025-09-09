package com.java.test.junior.service.loader;

import com.java.test.junior.model.ExtendedUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

public interface LoaderService {
    Mono<Void> load(String fileName, ExtendedUserDetails userDetails);
}
