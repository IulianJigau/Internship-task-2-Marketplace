package com.java.test.junior.service.User;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;

public interface UserService {
    User getUserById(Long userId);

    PaginationResponse<?> getUsersPage(Integer page, Integer size, Boolean refresh, Boolean isDeleted);

    User createUser(UserDTO user);

    void update(Long userId, String username, String password, ExtendedUserDetails userDetails);

    void deleteUser(Long userId, ExtendedUserDetails userDetails);

    void clearDeletedUsers();
}
