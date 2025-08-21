package com.java.test.junior.service.RoleService;

import com.java.test.junior.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    ResponseEntity<?> getRoles();

    ResponseEntity<?> createRole(String name);

    ResponseEntity<?> deleteRole(Long roleId);

    ResponseEntity<?> getUserRoles(Long userId);

    ResponseEntity<?> addUserRole(Long userId, String role);

    ResponseEntity<?> removeUserRole(Long userId, String role);
}
