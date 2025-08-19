package com.java.test.junior.service;

import com.java.test.junior.model.User;
import com.java.test.junior.model.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<User> getUserById(Long id);

    ResponseEntity<User> getUserByEmail(String email);

    ResponseEntity<List<User>> getUserPage(Integer page, Integer size);

    ResponseEntity<String> createUser(UserDTO user);

    ResponseEntity<String> updateUser(Long id, UserDTO user);

    ResponseEntity<String> deleteUser(Long id);
}
