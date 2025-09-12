package com.java.test.junior.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.exception.RequestFailException;
import com.java.test.junior.exception.ResourceValidationException;
import com.java.test.junior.model.response.ErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Consumer;

@Configuration
public class AppConfig {

    ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {

            @Override
            public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
                HttpStatusCode status = response.getStatusCode();
                return status.is4xxClientError() || status.is5xxServerError();
            }

            @Override
            public void handleError(@NonNull URI url,
                                    @NonNull HttpMethod method,
                                    @NonNull ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().is4xxClientError()) {
                    getError(response, details -> {
                        throw new ResourceValidationException(details);
                    });
                }
                if (response.getStatusCode().is5xxServerError()) {
                    getError(response, details -> {
                        throw new RequestFailException(details);
                    });
                }
            }
        });

        return restTemplate;
    }

    private void getError(ClientHttpResponse response, Consumer<String> handler) throws IOException {
        try (InputStream errorStream = response.getBody()) {
            ErrorResponse error = objectMapper.readValue(errorStream, ErrorResponse.class);
            handler.accept(error.getDetails());
        }
    }
}
