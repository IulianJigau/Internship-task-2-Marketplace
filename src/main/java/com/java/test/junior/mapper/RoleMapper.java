package com.java.test.junior.mapper;

import com.java.test.junior.model.Role;

import java.util.List;

public interface RoleMapper {

    boolean exists(String name);

    List<Role> findAll();

    void insert(String name);

    int delete(Long id);

    boolean existsUserRole(Long id, String role);

    List<String> findUserRoles(Long id);

    void insertUserRole(Long id, String role);

    int deleteUserRole(Long id, String role);
}
