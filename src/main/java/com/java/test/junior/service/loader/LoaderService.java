package com.java.test.junior.service.loader;

import com.java.test.junior.model.ExtendedUserDetails;
import reactor.core.publisher.Mono;

public interface LoaderService {
    Mono<Void> load(ExtendedUserDetails userDetails);
}
