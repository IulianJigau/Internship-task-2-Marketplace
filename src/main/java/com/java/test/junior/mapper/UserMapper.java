package com.java.test.junior.mapper;

import com.java.test.junior.model.User;
import com.java.test.junior.model.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User findById(Long id);

    User findByEmail(String email);

    List<User> getPage(@Param("page") Integer page, @Param("size") Integer size);

    void insert(UserDTO user);

    int update(@Param("id") Long id, @Param("user") UserDTO user);

    int delete(Long id);

    List<String> getRoles(Long id);

    void insertRole(Long id, String role);

    int deleteRole(Long id, String role);
}
