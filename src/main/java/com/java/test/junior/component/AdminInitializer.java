package com.java.test.junior.component;

import com.java.test.junior.model.User.UserDTO;
import com.java.test.junior.service.role.RoleService;
import com.java.test.junior.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserService userService;
    private final RoleService roleService;

    private final String email = System.getenv("INIT_EMAIL");
    private final String password = System.getenv("INIT_PASSWORD");

    @Value("${junior.admin-role}")
    private String adminRole;

    @PostConstruct
    public void init() {
        if (email == null || password == null) {
            return;
        }
        try {
            Long userId = userService.createUser(new UserDTO(email, adminRole, password)).getId();
            Integer roleId = roleService.createRole(adminRole).getId();
            roleService.assignUserRole(userId, roleId);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}