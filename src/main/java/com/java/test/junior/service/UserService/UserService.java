package com.java.test.junior.service.UserService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getUserById(Long userId);

    ResponseEntity<?> getUserPage(Integer page, Integer size);

    ResponseEntity<?> createUser(UserDTO user);

    ResponseEntity<?> updateUsername(Long userId, String username, ExtendedUserDetails userDetails);

    ResponseEntity<?> updatePassword(Long userId, String password, ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteUser(Long userId, ExtendedUserDetails userDetails);
}
