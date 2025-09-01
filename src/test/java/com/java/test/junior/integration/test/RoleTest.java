package com.java.test.junior.integration.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
public class RoleTest {

    private final MockHttpSession session;
    private final MockMvc mockMvc;

    public void checkGetRoles() throws Exception {
        mockMvc.perform(get("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkCreateRole() throws Exception {
        mockMvc.perform(post("/roles?name=basic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public void checkDeleteRole() throws Exception {
        mockMvc.perform(delete("/roles/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }
}
