package com.java.test.junior.integration.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductTest {

    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkGetProductById() throws Exception {
        log.debug("Check get product by id");

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkGetProductsPage() throws Exception {
        log.debug("Check get products page");

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkGetDeletedProductsPage() throws Exception {
        log.debug("Check get deleted products page");

        mockMvc.perform(get("/products/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkCreateProduct() throws Exception {
        log.debug("Check create product");

        String body = """
                    {
                        "name": "tom",
                        "price": 11.1,
                        "description": "jerry"
                    }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .session(session))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public void checkUpdateProduct() throws Exception {
        log.debug("Check update product");

        String body = """
                    {
                        "name": "pen",
                        "price": 2.5,
                        "description": "Write all you want"
                    }
                """;

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkDeleteProduct() throws Exception {
        log.debug("Check delete product");

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkClearDeleteProducts() throws Exception {
        log.debug("Check clear delete products");

        mockMvc.perform(delete("/products/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetReviewsPage() throws Exception {
        log.debug("Check get reviews page");

        mockMvc.perform(get("/products/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkAddReview() throws Exception {
        log.debug("Check add review");

        mockMvc.perform(post("/products/1/reviews?isLiked=true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }
}
