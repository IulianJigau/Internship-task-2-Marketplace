package com.java.test.junior.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
@Setter
public class LoaderTestService {

    private static final Logger logger = LoggerFactory.getLogger(LoaderTestService.class);
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkLoadProducts() {
        try {
            MvcResult result =
                    mockMvc.perform(post("/loading/products")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isCreated())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Product loading: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
