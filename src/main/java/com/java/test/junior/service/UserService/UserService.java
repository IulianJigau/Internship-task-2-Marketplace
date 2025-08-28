package com.java.test.junior.service.UserService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getUserById(Long userId);

    ResponseEntity<?> getUsersPage(Integer page, Integer size, Boolean isDeleted);

    ResponseEntity<?> createUser(UserDTO user);

    ResponseEntity<?> update(Long userId, String username, String password, ExtendedUserDetails userDetails);

    ResponseEntity<?> deleteUser(Long userId, ExtendedUserDetails userDetails);

    ResponseEntity<?> clearDeletedUsers();
}
