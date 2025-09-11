package com.java.test.junior.service.user;

import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.response.PaginationResponse;
import com.java.test.junior.model.user.User;
import com.java.test.junior.model.user.UserDTO;

public interface UserService {

    Boolean existsUserId(Long id);

    Boolean existsUserEmail(String email);

    User getUserByEmail(String email);

    User getUserById(Long userId);

    PaginationResponse<?> getUsersPage(PaginationOptionsDTO paginationOptions, Boolean isDeleted);

    User createUser(UserDTO user);

    void updateUser(Long userId, String username, String password, ExtendedUserDetails userDetails);

    void deleteUser(Long userId, ExtendedUserDetails userDetails);

    void clearDeletedUsers();
}