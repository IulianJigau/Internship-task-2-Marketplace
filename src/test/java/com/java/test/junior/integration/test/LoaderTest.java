package com.java.test.junior.integration.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
@Log4j2
public class LoaderTest {
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkLoadProducts() throws Exception{
        log.debug("Check load products");

        mockMvc.perform(post("/loader/products/0/products.csv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public void checkGetProviders() throws Exception{
        log.debug("Check get providers");

        mockMvc.perform(get("/loader/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetProviderFiles() throws Exception{
        log.debug("Check get provider files");

        mockMvc.perform(get("/loader/providers/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

}
