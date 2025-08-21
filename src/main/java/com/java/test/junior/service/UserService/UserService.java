package com.java.test.junior.service.UserService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<?> getUserById(Long userId);

    ResponseEntity<?> getUserPage(Integer page, Integer size);

    ResponseEntity<?> createUser(UserDTO user);

    ResponseEntity<?> updateUser(Long userId, UserDTO user, ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteUser(Long userId,  ExtendedUserDetails userDetails);
}
