package com.java.test.junior.mapper;

import com.java.test.junior.model.user.User;
import com.java.test.junior.model.user.UserDTO;

import java.util.List;

public interface UserMapper {

    boolean exists(Long id);

    boolean existsEmail(String email);

    User find(Long id);

    User findByEmail(String email);

    List<User> getPage(Integer page, Integer size, Boolean isDeleted);

    long getTotalEntries(Boolean isDeleted);

    long insert(UserDTO user);

    int update(Long id, String username, String password);

    int delete(Long id);

    void clearDeleted();
}
