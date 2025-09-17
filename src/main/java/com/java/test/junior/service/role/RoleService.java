package com.java.test.junior.service.role;

import com.java.test.junior.model.Role;

import java.util.List;

public interface RoleService {

    Boolean existsRoleId(Integer id);

    Boolean existsRoleName(String name);

    Role findRoleById(Integer id);

    Role findRoleByName(String name);

    List<Role> getRoles();

    Role createRole(String name);

    void deleteRole(Integer roleId);

    Boolean existsUserRole(Long userId, Integer roleId);

    List<Role> getUserRoles(Long userId);

    void assignUserRole(Long userId, Integer roleId);

    void removeUserRole(Long userId, Integer roleId);
}
