package com.java.test.junior.service.user;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.RequestResponse.PaginationResponse;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;

public interface UserService {
    User getUserById(Long userId);

    PaginationResponse<?> getUsersPage(PaginationOptionsDTO paginationOptions, Boolean isDeleted);

    User createUser(UserDTO user);

    void update(Long userId, String username, String password, ExtendedUserDetails userDetails);

    void deleteUser(Long userId, ExtendedUserDetails userDetails);

    void clearDeletedUsers();
}