package com.java.test.junior.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("roleChecker")
public class RoleChecker {
    @Value("${junior.admin-role}")
    private String adminRole;

    public boolean hasAdminRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + adminRole));
    }
}
