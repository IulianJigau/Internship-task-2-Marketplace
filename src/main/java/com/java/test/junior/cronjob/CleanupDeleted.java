package com.java.test.junior.cronjob;

import com.java.test.junior.service.product.ProductService;
import com.java.test.junior.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanupDeleted {

    private final UserService userService;
    private final ProductService productService;

    @Scheduled(cron = "0 0 0 1,14,28 * *")
    public void cleanup() {
        userService.clearDeletedUsers();
        productService.clearDeletedProducts();
    }
}
