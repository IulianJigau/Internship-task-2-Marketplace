package com.java.test.junior.service.RoleService;

import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<?> getRoles();

    ResponseEntity<?> createRole(String name);

    ResponseEntity<?> deleteRole(Long roleId);

    ResponseEntity<?> getUserRoles(Long userId);

    ResponseEntity<?> addUserRole(Long userId, String role);

    ResponseEntity<?> removeUserRole(Long userId, String role);
}
