package com.java.test.junior.service.UserService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<User> getUserById(Long userId);

    ResponseEntity<List<User>> getUserPage(Integer page, Integer size);

    ResponseEntity<String> createUser(UserDTO user);

    ResponseEntity<String> updateUser(Long userId, UserDTO user, ExtendedUserDetails userDetails);

    ResponseEntity<String> deleteUser(Long userId,  ExtendedUserDetails userDetails);
}
