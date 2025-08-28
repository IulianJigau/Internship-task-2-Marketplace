package com.java.test.junior;

import com.java.test.junior.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class JuniorApplicationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private final LoaderTestService loaderTestService;
    private final ProductTestService productTestService;
    private final RoleTestService roleTestService;
    private final SessionTestService sessionTestService;
    private final UserTestService userTestService;

    @Autowired
    JuniorApplicationTest(
            LoaderTestService loaderTestService,
            ProductTestService productTestService,
            RoleTestService roleTestService,
            SessionTestService sessionTestService,
            UserTestService userTestService) {
        this.loaderTestService = loaderTestService;
        this.productTestService = productTestService;
        this.roleTestService = roleTestService;
        this.sessionTestService = sessionTestService;
        this.userTestService = userTestService;

        MockHttpSession session = new MockHttpSession();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void RunTests() {
        sessionTestService.checkLoginAsAdmin();
        userTestService.checkAddUsers();
        userTestService.checkGetUsers();
        userTestService.checkUpdateUsername(2);
        userTestService.checkUpdatePassword(2);
        userTestService.checkGetUser(2);
        roleTestService.checkCreateRole("basic");
        roleTestService.checkGetRoles();
        userTestService.checkAssignRole(2, 2);
        userTestService.checkRemoveUserRole(2, 2);
        roleTestService.checkDeleteRole(2);
        loaderTestService.checkLoadProducts();
        productTestService.checkAddProducts();
        productTestService.checkGetProducts();
        productTestService.checkUpdateProducts(2);
        productTestService.checkGetProduct(2);
        productTestService.checkAddReview(2, true);
        productTestService.checkGetReview(2);
        productTestService.checkDeleteProduct(2);
        sessionTestService.checkLogout();
    }
}