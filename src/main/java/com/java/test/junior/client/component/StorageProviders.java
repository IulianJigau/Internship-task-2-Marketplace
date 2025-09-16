package com.java.test.junior.client.component;

import com.java.test.junior.model.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "junior")
public class StorageProviders {
    private List<Provider> providers;

    @Bean
    public List<Provider> getProviders() {
        return providers;
    }
}
