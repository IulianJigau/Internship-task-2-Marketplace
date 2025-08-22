package com.java.test.junior.mapper;

import com.java.test.junior.model.Role;

import java.util.List;

public interface RoleMapper {

    boolean exists(Integer id);

    boolean existsName(String name);

    Role find(Integer id);

    Role findByName(String name);

    List<Role> findAll();

    void insert(String name);

    int delete(Integer id);

    boolean existsUserRole(Long userId, Integer roleId);

    List<String> findUserRoles(Long userId);

    void insertUserRole(Long userId, Integer roleId);

    int deleteUserRole(Long userId, Integer roleId);
}
