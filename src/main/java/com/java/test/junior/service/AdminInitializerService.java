package com.java.test.junior.service;

import com.java.test.junior.model.user.UserDTO;
import com.java.test.junior.service.role.RoleService;
import com.java.test.junior.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminInitializerService {

    private final UserService userService;
    private final RoleService roleService;

    private final String email = System.getenv("INIT_EMAIL");
    private final String password = System.getenv("INIT_PASSWORD");

    @Value("${junior.admin-role}")
    private String adminRole;

    @PostConstruct
    public void init() {
        if (email == null) {
            log.info("Admin user initialization skipped. No email was provided.");
            return;
        }

        Long userId;
        if (userService.existsUserEmail(email)) {
            userId = userService.getUserByEmail(email).getId();
            addUserRole(userId);
            return;
        }

        if (password == null) {
            log.info("Admin user initialization skipped. No password was provided.");
            return;
        }

        userId = userService.createUser(new UserDTO(email, adminRole, password)).getId();
        addUserRole(userId);
    }

    private void addUserRole(Long userId) {
        Integer roleId;
        if (roleService.existsRoleName(adminRole)) {
            roleId = roleService.findRoleByName(adminRole).getId();
        } else {
            roleId = roleService.createRole(adminRole).getId();
        }

        if (!roleService.existsUserRole(userId, roleId)) {
            roleService.assignUserRole(userId, roleId);
        }
    }
}