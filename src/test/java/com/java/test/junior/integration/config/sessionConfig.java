package com.java.test.junior.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;

@Configuration
public class sessionConfig {
    MockHttpSession session = new MockHttpSession();

    @Bean
    MockHttpSession session() {
        return session;
    }
}
