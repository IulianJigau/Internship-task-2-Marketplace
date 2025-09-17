package com.java.test.junior.integration.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActionHistoryTest {

    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkGetActionHistoryPage() throws Exception{
        log.debug("Check get action history page");

        mockMvc.perform(get("/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }
}
