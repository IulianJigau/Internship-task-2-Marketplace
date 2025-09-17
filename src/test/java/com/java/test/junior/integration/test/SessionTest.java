package com.java.test.junior.integration.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionTest {

    private final MockHttpSession session;
    private final MockMvc mockMvc;

    public void checkLogin() throws Exception {
        log.debug("Check login");

        String body = """
                    {
                        "email": "admin@example.com",
                        "password": "Password123"
                    }
                """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkLogout() throws Exception {
        log.debug("Check logout");

        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }
}
