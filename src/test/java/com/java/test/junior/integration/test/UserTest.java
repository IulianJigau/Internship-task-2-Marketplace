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
public class UserTest {

    private final MockHttpSession session;
    private final MockMvc mockMvc;

    public void checkGetCurrentUser() throws Exception {
        mockMvc.perform(get("/users/self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetUserById() throws Exception {
        mockMvc.perform(get("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetUsersPage() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetUserProductsPage() throws Exception {
        mockMvc.perform(get("/users/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetDeletedUsersPage() throws Exception {
        mockMvc.perform(get("/users/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkCreateUser() throws Exception {
        String body = """
                    {
                        "email": "Basic@example",
                        "username": "Test",
                        "password": "Test123"
                    }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .session(session))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public void checkUpdateUser() throws Exception {
        String body = "Qwerty";

        mockMvc.perform(put("/users/2?username=Andrey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkClearDeletedUsers() throws Exception {
        mockMvc.perform(delete("/users/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkGetUserRoles() throws Exception {
        mockMvc.perform(get("/users/2/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void checkAssignUserRole() throws Exception {
        mockMvc.perform(post("/users/2/roles?roleId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public void checkRemoveUserRole() throws Exception {
        mockMvc.perform(delete("/users/2/roles?roleId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }

    public void checkGetReviewsPage() throws Exception {
        mockMvc.perform(get("/users/2/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

    }
}
