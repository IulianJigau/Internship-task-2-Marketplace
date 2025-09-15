package com.java.test.junior.integration.test;

import lombok.RequiredArgsConstructor;
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
public class LoaderTest {
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkLoadProducts() throws Exception{
        mockMvc.perform(post("/loader/products/0/products.csv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public void checkGetProviders() throws Exception{
        mockMvc.perform(get("/loader/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetProviderFiles() throws Exception{
        mockMvc.perform(get("/loader/providers/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

}
