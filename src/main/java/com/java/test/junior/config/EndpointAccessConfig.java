package com.java.test.junior.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Configuration
public class EndpointAccessConfig {

    @Bean
    public List<RequestMatcher> permittedEndpointMatchers() {
        return
                List.of(
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/users"),
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/login"),
                        PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**"),
                        PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui.html"),
                        PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**")
                );
    }

    ;
}