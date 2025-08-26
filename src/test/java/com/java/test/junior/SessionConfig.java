package com.java.test.junior;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;

@Configuration
@Data
public class SessionConfig {

    @Bean
    public MockHttpSession session(){
        return new MockHttpSession();
    }
}
