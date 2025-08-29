package com.java.test.junior.service.LoaderService;

import com.java.test.junior.model.ExtendedUserDetails;
import org.springframework.http.ResponseEntity;

public interface LoaderService {
    void loadProducts(ExtendedUserDetails userDetails);
}
