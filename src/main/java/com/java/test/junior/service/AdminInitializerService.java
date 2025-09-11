package com.java.test.junior.service;

import com.java.test.junior.model.user.UserDTO;
import com.java.test.junior.service.role.RoleService;
import com.java.test.junior.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInitializerService {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializerService.class);

    private final UserService userService;
    private final RoleService roleService;

    private final String email = System.getenv("INIT_EMAIL");
    private final String password = System.getenv("INIT_PASSWORD");

    @Value("${junior.admin-role}")
    private String adminRole;

    @PostConstruct
    public void init() {
        if (email == null) {
            logger.info("Admin user initialization skipped. No email was provided.");
        }

        Long userId;
        if (userService.existsUserEmail(email)) {
            userId = userService.getUserByEmail(email).getId();
        } else {
            if (password == null) {
                logger.info("Admin user initialization skipped. No password was provided.");
            }
            userId = userService.createUser(new UserDTO(email, adminRole, password)).getId();
        }

        Integer roleId = roleService.existsRoleName(adminRole) ?
                roleService.findRoleByName(adminRole).getId() :
                roleService.createRole(adminRole).getId();

        if (!roleService.existsUserRole(userId, roleId)) {
            roleService.assignUserRole(userId, roleId);
        }
    }
}