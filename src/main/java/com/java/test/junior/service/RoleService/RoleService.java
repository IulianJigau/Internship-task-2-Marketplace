package com.java.test.junior.service.RoleService;

import com.java.test.junior.model.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();

    void createRole(String name);

    void deleteRole(Integer roleId);

    List<String> getUserRoles(Long userId);

    void assignUserRole(Long userId, Integer roleId);

    void removeUserRole(Long userId, Integer roleId);
}
