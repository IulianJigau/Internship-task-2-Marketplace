package com.java.test.junior.mapper;

import com.java.test.junior.model.Role;

import java.util.List;

public interface RoleMapper {
    List<Role> findAll();

    void insert(String name);

    int delete(Long id);
}
