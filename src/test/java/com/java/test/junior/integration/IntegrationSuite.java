package com.java.test.junior.integration;

import com.java.test.junior.integration.test.ProductTest;
import com.java.test.junior.integration.test.RoleTest;
import com.java.test.junior.integration.test.SessionTest;
import com.java.test.junior.integration.test.UserTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class IntegrationSuite {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private ProductTest productTest;
    @Autowired
    private RoleTest roleTest;
    @Autowired
    private SessionTest sessionTest;
    @Autowired
    private UserTest userTest;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void runTests() {
        try {
            sessionTest.checkLogin();

            userTest.checkGetCurrentUser();
            userTest.checkCreateUser();
            userTest.checkGetUserById();
            userTest.checkUpdateUser();
            userTest.checkGetUsersPage();

            productTest.checkCreateProduct();
            productTest.checkGetProductsPage();
            productTest.checkGetProductById();
            productTest.checkUpdateProduct();

            userTest.checkGetUserProductsPage();

            productTest.checkAddReview();
            productTest.checkGetReviewsPage();

            userTest.checkGetReviewsPage();

            productTest.checkAddReview();

            roleTest.checkCreateRole();
            roleTest.checkGetRoles();

            userTest.checkAssignUserRole();
            userTest.checkGetUserRoles();
            userTest.checkRemoveUserRole();

            roleTest.checkDeleteRole();

            productTest.checkDeleteProduct();
            productTest.checkGetDeletedProductsPage();
            productTest.checkClearDeleteProducts();

            userTest.checkDeleteUser();
            userTest.checkGetDeletedUsersPage();
            userTest.checkClearDeletedUsers();

            sessionTest.checkLogout();
        } catch (Exception e) {
            throw new AssertionError("Test failed due to: " + e.getMessage());
        }
    }
}