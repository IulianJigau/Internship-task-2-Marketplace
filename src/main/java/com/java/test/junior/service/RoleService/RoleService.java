package com.java.test.junior.service.RoleService;

import com.java.test.junior.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    ResponseEntity<List<Role>> getRoles();

    ResponseEntity<String> createRole(String name);

    ResponseEntity<String> deleteRole(Long roleId);

    ResponseEntity<List<String>> getUserRoles(Long userId);

    ResponseEntity<String> addUserRole(Long userId, String role);

    ResponseEntity<String> removeUserRole(Long userId, String role);
}
