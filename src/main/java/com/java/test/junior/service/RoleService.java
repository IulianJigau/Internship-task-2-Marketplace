package com.java.test.junior.service;

import com.java.test.junior.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    ResponseEntity<List<Role>> getRoles();

    ResponseEntity<String> createRole(String name);

    ResponseEntity<String> deleteRole(Long id);

    ResponseEntity<List<String>> getUserRoles(Long id);

    ResponseEntity<String> addUserRoles(Long id, List<String> roles);

    ResponseEntity<String> removeUserRoles(Long id, List<String> roles);
}
