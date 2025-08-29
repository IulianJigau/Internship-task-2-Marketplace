package com.java.test.junior.service.UserService;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User getUserById(Long userId);

    PaginationResponse<User> getUsersPage(Integer page, Integer size, Boolean isDeleted);

    User createUser(UserDTO user);

    void update(Long userId, String username, String password, ExtendedUserDetails userDetails);

    void deleteUser(Long userId, ExtendedUserDetails userDetails);

    void clearDeletedUsers();
}
