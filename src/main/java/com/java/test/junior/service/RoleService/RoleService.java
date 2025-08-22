package com.java.test.junior.service.RoleService;

import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<?> getRoles();

    ResponseEntity<?> createRole(String name);

    ResponseEntity<?> deleteRole(Integer roleId);

    ResponseEntity<?> getUserRoles(Long userId);

    ResponseEntity<?> addUserRole(Long userId, Integer roleId);

    ResponseEntity<?> removeUserRole(Long userId, Integer roleId);
}
