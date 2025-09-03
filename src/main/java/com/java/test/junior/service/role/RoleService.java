package com.java.test.junior.service.role;

import com.java.test.junior.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();

    Role createRole(String name);

    void deleteRole(Integer roleId);

    List<String> getUserRoles(Long userId);

    void assignUserRole(Long userId, Integer roleId);

    void removeUserRole(Long userId, Integer roleId);
}
