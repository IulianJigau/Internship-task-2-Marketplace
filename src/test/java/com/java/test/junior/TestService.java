package com.java.test.junior;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    private final MockMvc mockMvc;

    public void checkLogin(MockHttpSession session) {
        try {
            String body = """
                        {
                            "email": "admin@example.com",
                            "password": "Password123"
                        }
                    """;

            MvcResult result =
                    mockMvc.perform(post("/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Login: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkLoadProducts(MockHttpSession session) {
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

    public void checkGetProducts(MockHttpSession session) {
        try {
            MvcResult result =
                    mockMvc.perform(get("/products?page=1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();
            String content = result.getResponse().getContentAsString();

            logger.info("Get products: {}\nContent: {}", status, content);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkUpdateProducts(MockHttpSession session, int id) {
        try {
            String body = """
                        {
                            "name": "pen",
                            "price": 2.5,
                            "description": "Write all you want"
                        }
                    """;

            MvcResult result =
                    mockMvc.perform(put("/products/" + id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Update product {}: {}", id, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetProduct(MockHttpSession session, int id) {
        try {
            MvcResult result =
                    mockMvc.perform(get("/products/" + id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();
            String content = result.getResponse().getContentAsString();

            logger.info("Get product {}: {}\nContent: {}", id, status, content);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkDeleteProduct(MockHttpSession session, int id) {
        try {
            MvcResult result =
                    mockMvc.perform(delete("/products/" + id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Delete product {}: {}", id, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
