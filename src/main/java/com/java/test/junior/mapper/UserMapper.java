package com.java.test.junior.mapper;

import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;

import java.util.List;

public interface UserMapper {

    boolean exists(Long id);

    User findById(Long id);

    User findByEmail(String email);

    List<User> getPage(Integer page, Integer size);

    void insert(UserDTO user);

    int update(Long id, UserDTO user);

    int delete(Long id);
}
