package com.java.test.junior.service;

import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.User;
import com.java.test.junior.model.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attrs != null) ? attrs.getRequest() : null;
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attrs != null) ? attrs.getResponse() : null;
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        try {
            User user = userMapper.findById(id);
            return (user != null)
                    ? ResponseEntity.ok(user)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<User> getUserByEmail(String email) {
        try {
            User user = userMapper.findByEmail(email);
            return (user != null)
                    ? ResponseEntity.ok(user)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<User>> getUserPage(Integer page, Integer size) {
        try {
            List<User> users = userMapper.getPage(
                    page,
                    size);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> createUser(UserDTO user) {
        if (userMapper.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This email is tied to another account");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insert(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> updateUser(Long id, UserDTO userDTO) {
        Authentication authentication = getAuthentication();
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        User user = userMapper.findById(id);
        if (user != null) {
            boolean isOwner = user.getEmail().equals(authentication.getName());

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || isOwner) {
                try {
                    if (isOwner) {
                        new SecurityContextLogoutHandler().logout(request, response, authentication);
                    }
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userMapper.update(id, userDTO);
                    return ResponseEntity.ok().build();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return ResponseEntity.internalServerError().build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        Authentication authentication = getAuthentication();
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        User user = userMapper.findById(id);
        if (user != null) {
            boolean isOwner = user.getEmail().equals(authentication.getName());

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || isOwner) {
                try {
                    if (isOwner) {
                        new SecurityContextLogoutHandler().logout(request, response, authentication);
                    }
                    userMapper.delete(id);
                    return ResponseEntity.ok().build();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return ResponseEntity.internalServerError().build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.notFound().build();
    }
}
