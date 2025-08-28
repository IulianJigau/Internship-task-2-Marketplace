package com.java.test.junior.service;

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
public class ProductTestService {

    private static final Logger logger = LoggerFactory.getLogger(ProductTestService.class);
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkAddProducts() {
        try {
            String body = """
                        {
                            "name": "tom",
                            "price": 11.1,
                            "description": "jerry"
                        }
                    """;

            MvcResult result =
                    mockMvc.perform(post("/products")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .session(session))
                            .andExpect(status().isCreated())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Add product: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetProducts() {
        try {
            MvcResult result =
                    mockMvc.perform(get("/products?page=1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Get products: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkUpdateProducts(int productId) {
        try {
            String body = """
                        {
                            "name": "pen",
                            "price": 2.5,
                            "description": "Write all you want"
                        }
                    """;

            MvcResult result =
                    mockMvc.perform(put("/products/" + productId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Update product {}: {}", productId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetProduct(int productId) {
        try {
            MvcResult result =
                    mockMvc.perform(get("/products/" + productId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Get product {}: {}", productId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkAddReview(int productId, boolean isLiked) {
        try {
            MvcResult result =
                    mockMvc.perform(post("/products/" + productId + "/reviews?isLiked=" + isLiked)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isCreated())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Add review on product {}: {}", productId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetReview(int productId) {
        try {
            MvcResult result =
                    mockMvc.perform(get("/products/" + productId + "/reviews?page=1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Get product {} reviews: {}", productId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkDeleteProduct(int productId) {
        try {
            MvcResult result =
                    mockMvc.perform(delete("/products/" + productId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Delete product {}: {}", productId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
