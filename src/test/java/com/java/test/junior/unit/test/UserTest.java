package com.java.test.junior.unit.test;

import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.User.User;
import com.java.test.junior.model.User.UserDTO;
import com.java.test.junior.service.user.UserService;
import com.java.test.junior.service.user.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Service
public class UserTest {
    private final UserMapper userMapper;
    private final UserService userService;

    UserTest() {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImp(passwordEncoder, userMapper);
    }

    @Test
    void checkGetUserById_valid() {
        User user = new User();
        user.setId(1L);
        user.setIsDeleted(false);

        when(userMapper.find(1L)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void checkGetUserById_absent() {
        when(userMapper.find(1L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void checkGetUserById_deleted() {
        User user = new User();
        user.setId(1L);
        user.setIsDeleted(true);

        when(userMapper.find(1L)).thenReturn(user);

        assertThrows(ResourceDeletedException.class, () -> userService.getUserById(1L));
    }

    @Test
    void createUser_valid() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("name@email.com");
        userDTO.setPassword("password");

        User user = new User();
        user.setId(1L);

        when(userMapper.existsEmail(userDTO.getEmail())).thenReturn(false);
        when(userMapper.insert(userDTO)).thenReturn(1L);
        when(userMapper.find(1L)).thenReturn(user);

        User result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void createUser_conflict() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("name@email.com");
        userDTO.setPassword("password");

        when(userMapper.existsEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> userService.createUser(userDTO));
    }
}
