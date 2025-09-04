package com.java.test.junior.service.user;

import com.java.test.junior.component.RoleChecker;
import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleChecker roleChecker;

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.find(userId);
        if (user == null) {
            throw new ResourceNotFoundException("The requested user was not found.");
        }
        if (user.getIsDeleted()) {
            throw new ResourceDeletedException("The requested user was deleted.");
        }
        return user;
    }

    @Override
    public PaginationResponse<?> getUsersPage(PaginationOptionsDTO paginationOptions, Boolean isDeleted) {
        List<User> users = userMapper.getPage(paginationOptions.getPage(), paginationOptions.getPageSize(), isDeleted);
        long entries = -1L;
        if (paginationOptions.getRefresh()) {
            entries = userMapper.getTotalEntries(isDeleted);
        }
        return new PaginationResponse<>(entries, users);
    }

    @Override
    public User createUser(UserDTO user) {
        if (userMapper.existsEmail(user.getEmail())) {
            throw new ResourceConflictException("The provided email is already in use.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Long newId = userMapper.insert(user);
        return userMapper.find(newId);
    }

    public void checkOwnershipAndRun(Action action, Long userId, ExtendedUserDetails userDetails) {
        if (!roleChecker.hasAdminRole(userDetails) && !userId.equals(userDetails.getId())) {
            throw new AccessDeniedException("You must be the owner of this account to perform this operation.");
        }

        int result = action.execute();
        if (result == 0) {
            throw new ResourceNotFoundException("The requested user was not found.");
        }
    }

    @Override
    public void update(Long userId, String username, String password, ExtendedUserDetails userDetails) {
        checkOwnershipAndRun(
                () -> userMapper.update(userId, username, password != null ? passwordEncoder.encode(password) : null),
                userId, userDetails);
    }

    @Override
    public void deleteUser(Long userId, ExtendedUserDetails userDetails) {
        checkOwnershipAndRun(() -> userMapper.delete(userId), userId, userDetails);
    }

    @Override
    public void clearDeletedUsers() {
        userMapper.clearDeleted();
    }

    @FunctionalInterface
    public interface Action {
        int execute();
    }
}
