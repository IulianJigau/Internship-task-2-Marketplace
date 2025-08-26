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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
@RequiredArgsConstructor
@Setter
public class RoleTestService {

    private static final Logger logger = LoggerFactory.getLogger(RoleTestService.class);
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkCreateRole(String name) {
        try {
            MvcResult result =
                    mockMvc.perform(post("/roles?name=" + name)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isCreated())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Add a role: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetRoles() {
        try {
            MvcResult result =
                    mockMvc.perform(get("/roles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Get roles: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkDeleteRole(int roleId) {
        try {
            MvcResult result =
                    mockMvc.perform(delete("/roles/" + roleId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Delete role {}: {}", roleId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}