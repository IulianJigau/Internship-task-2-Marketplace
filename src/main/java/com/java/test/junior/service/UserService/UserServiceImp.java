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
            User user = userMapper.findById(userId);
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
            if (userMapper.findByEmail(user.getEmail()) != null) {
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
            User user = userMapper.findById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            if (!isAdmin(userDetails) && !user.getId().equals(userDetails.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            action.execute();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> updateUser(Long userId, UserDTO userDTO, ExtendedUserDetails userDetails) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return checkOwnershipAndRun(() -> userMapper.update(userId, userDTO), userId, userDetails);
    }

    @Override
    public ResponseEntity<?> deleteUser(Long userId, ExtendedUserDetails userDetails) {
        return checkOwnershipAndRun(() -> userMapper.delete(userId), userId, userDetails);
    }

    @FunctionalInterface
    public interface Action {
        void execute() throws Exception;
    }
}
