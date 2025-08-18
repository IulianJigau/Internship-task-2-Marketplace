package com.java.test.junior.service;

import com.java.test.junior.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    ResponseEntity<List<Role>> getRoles();

    ResponseEntity<String> createRole(String name);

    ResponseEntity<String> deleteRole(Long id);
}
