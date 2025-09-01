package com.java.test.junior;

import com.java.test.junior.model.User.User;
import com.java.test.junior.service.UserService.UserService;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.exception.*;

import com.java.test.junior.service.UserService.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    private UserMapper userMapper;
    private UserServiceImp userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImp(passwordEncoder, userMapper);
    }

    @Test
    void shouldReturnUser_whenUserExistsAndNotDeleted() {
        User user = new User();
        user.setId(1L);
        user.setIsDeleted(false);

        when(userMapper.find(1L)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowNotFound_whenUserIsNull() {
        when(userMapper.find(2L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void shouldThrowDeleted_whenUserIsMarkedDeleted() {
        User user = new User();
        user.setId(3L);
        user.setIsDeleted(true);

        when(userMapper.find(3L)).thenReturn(user);

        assertThrows(ResourceDeletedException.class, () -> userService.getUserById(3L));
    }

    @Test
    void shouldThrowDatabaseFail_onDataAccessException() {
        when(userMapper.find(4L)).thenThrow(new org.springframework.dao.DataAccessResourceFailureException("fail"));

        assertThrows(DatabaseFailException.class, () -> userService.getUserById(4L));
    }
}
