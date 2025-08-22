package com.java.test.junior.service.UserService;

import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
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
            if (user != null) {
                user.setPassword("Hidden");
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getUserPage(Integer page, Integer size) {
        try {
            List<User> users = userMapper.getPage(page, size);
            for (User user : users) {
                user.setPassword("Hidden");
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> createUser(UserDTO user) {
        try {
            if (userMapper.existsEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insert(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> checkOwnershipAndRun(Action action, Long userId, ExtendedUserDetails userDetails) {
        try {
            if (!isAdmin(userDetails) && !userId.equals(userDetails.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<?> updateUsername(Long userId, String username, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> userMapper.updateUsername(userId, username), userId, userDetails);
    }

    @Override
    public ResponseEntity<?> updatePassword(Long userId, String password, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> userMapper.updatePassword(userId, passwordEncoder.encode(password)), userId, userDetails);
    }

    @Override
    public ResponseEntity<?> deleteUser(Long userId, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> userMapper.delete(userId), userId, userDetails);
    }

    @FunctionalInterface
    public interface Action {
        int execute() throws Exception;
    }
}
