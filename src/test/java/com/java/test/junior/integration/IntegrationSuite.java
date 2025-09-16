package com.java.test.junior.integration;

import com.java.test.junior.integration.test.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
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

    @Container
    static GenericContainer<?> storageApp = new GenericContainer<>("storage-app")
            .withExposedPorts(8082)
            .withEnv("STORAGE_PATH", "/data/output")
            .withFileSystemBind("C:/Temp/Output", "/data/output");

    @Autowired
    private ProductTest productTest;
    @Autowired
    private RoleTest roleTest;
    @Autowired
    private SessionTest sessionTest;
    @Autowired
    private UserTest userTest;
    @Autowired
    private LoaderTest loaderTest;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("junior.providers[0].id",
                () -> "0");
        registry.add("junior.providers[0].path",
                () -> "http://" + storageApp.getHost() + ":" + storageApp.getFirstMappedPort());
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

            loaderTest.checkGetProviders();
            loaderTest.checkGetProviderFiles();
            loaderTest.checkLoadProducts();

            sessionTest.checkLogout();
        } catch (Exception e) {
            throw new AssertionError("Test failed due to: " + e.getMessage());
        }
    }
}