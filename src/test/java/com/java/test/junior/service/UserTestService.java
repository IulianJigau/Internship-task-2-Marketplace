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
public class UserTestService {

    private static final Logger logger = LoggerFactory.getLogger(UserTestService.class);
    private final MockMvc mockMvc;
    private final MockHttpSession session;

    public void checkAddUsers() {
        try {
            String body = """
                        {
                            "email": "Basic@example",
                            "username": "Test",
                            "password": "Test123"
                        }
                    """;

            MvcResult result =
                    mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .session(session))
                            .andExpect(status().isCreated())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Add user: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetUsers() {
        try {
            MvcResult result =
                    mockMvc.perform(get("/users?page=1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Get users: {}", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkUpdateUsername(int userId) {
        try {
            MvcResult result =
                    mockMvc.perform(put("/users/" + userId + "?username=Andrey")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Update user {} username: {}", userId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkUpdatePassword(int userId) {
        try {
            String body = "Qwerty";

            MvcResult result =
                    mockMvc.perform(put("/users/" + userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Update user {} password: {}", userId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkGetUser(int userId) {
        try {
            MvcResult result =
                    mockMvc.perform(get("/users/" + userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Get user {}: {}", userId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkAssignRole(int userId, int roleId) {
        try {
            MvcResult result =
                    mockMvc.perform(post("/users/" + userId + "/roles?roleId=" + roleId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isCreated())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Assign role {} to user {}: {}", roleId, userId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void checkRemoveUserRole(int userId, int roleId) {
        try {
            MvcResult result =
                    mockMvc.perform(delete("/users/" + userId + "/roles?roleId=" + roleId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .session(session))
                            .andExpect(status().isOk())
                            .andReturn();

            int status = result.getResponse().getStatus();

            logger.info("Remove role {} from user {}: {}", roleId, userId, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
