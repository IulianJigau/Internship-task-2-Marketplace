package com.java.test.junior.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("roleChecker")
public class RoleChecker {
    @Value("${junior.storage.site-url}")
    private String adminRole;

    public boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + adminRole));
    }
}
