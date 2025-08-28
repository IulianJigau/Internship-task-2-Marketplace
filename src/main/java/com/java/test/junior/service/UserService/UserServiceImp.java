package com.java.test.junior.service.UserService;

import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponses.ErrorResponse;
import com.java.test.junior.model.RequestResponses.PaginationResponse;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private boolean isAdmin(ExtendedUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public ResponseEntity<?> getUserById(Long userId) {
        try {
            User user = userMapper.find(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ErrorResponse("The requested user was not found.")
                );
            }
            if (user.getIsDeleted()) {
                return ResponseEntity.status(HttpStatus.GONE).body(
                        new ErrorResponse("The requested user has been deleted.")
                );
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getUsersPage(Integer page, Integer size, Boolean isDeleted) {
        try {
            List<User> users = userMapper.getPage(page, size, isDeleted);
            Long entries = userMapper.getTotalEntries(isDeleted);
            return ResponseEntity.ok(
                    new PaginationResponse<>(entries, users)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> createUser(UserDTO user) {
        try {
            if (userMapper.existsEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ErrorResponse("The provided email is already in use.")
                );
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Long newId = userMapper.insert(user);
            User newUser = userMapper.find(newId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> checkOwnershipAndRun(Action action, Long userId, ExtendedUserDetails userDetails) {
        try {
            if (!isAdmin(userDetails) && !userId.equals(userDetails.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ErrorResponse("You must be the owner of this account to perform this operation.")
                );
            }

            return action.execute() > 0 ?
                    ResponseEntity.ok().build() :
                    ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> update(Long userId, String username, String password, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(
                () -> userMapper.update(userId, username, password != null ? passwordEncoder.encode(password) : null),
                userId, userDetails);
    }

    @Override
    public ResponseEntity<?> deleteUser(Long userId, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> userMapper.delete(userId), userId, userDetails);
    }

    @Override
    public ResponseEntity<?> clearDeletedUsers() {
        try {
            userMapper.clearDeleted();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @FunctionalInterface
    public interface Action {
        int execute() throws Exception;
    }
}
