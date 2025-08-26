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
public class SessionTestService {

    private static final Logger logger = LoggerFactory.getLogger(SessionTestService.class);
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkLoginAsAdmin() {
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

    public void checkLogout() {
        try {
            MvcResult result =
                    mockMvc.perform(post("/logout")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Logout: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}