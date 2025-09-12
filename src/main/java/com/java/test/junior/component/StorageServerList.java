package com.java.test.junior.component;

import com.java.test.junior.model.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class StorageServerList {

    @Value("${junior.server-list}")
    private final List<String> servers;

    @Bean
    @Qualifier("storageServers")
    public List<Resource> storageServers() {
        return IntStream.range(0, servers.size())
                .mapToObj(i -> new Resource(i, servers.get(i)))
                .collect(Collectors.toList());
    }
}
