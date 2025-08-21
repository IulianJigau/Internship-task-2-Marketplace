package com.java.test.junior;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JuniorApplicationTests {

    @Autowired
    private AdminUserCreator adminUserCreator;

    @Test
    void createAdminUser() {
        adminUserCreator.createAdminUser("admin@example.com", "admin", "password123");
    }
}
