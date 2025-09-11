package com.java.test.junior.component;

import com.java.test.junior.model.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StorageServerList {

    @Bean
    @Qualifier("storageServers")
    public List<Resource> storageServers() {
        return List.of(
                new Resource(0, "http://localhost:8082"),
                new Resource(1, "Placeholder")
        );
    }
}
