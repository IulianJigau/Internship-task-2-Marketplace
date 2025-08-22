package com.java.test.junior;

import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.User.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserCreator {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public void createAdminUser(String email, String username, String rawPassword) {
        if (!roleMapper.exists("Admin")) {
            roleMapper.insert("Admin"); // Assuming such a method exists
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setUsername(username);
        userDTO.setPassword(passwordEncoder.encode(rawPassword));

        if (!userMapper.exists(userMapper.findByEmail(userDTO.getEmail()).getId())) {
            userMapper.insert(userDTO);
        }

        Long userId = userMapper.findByEmail(email).getId();
        if (!roleMapper.existsUserRole(userId, "Admin")) {
            roleMapper.insertUserRole(userId, "Admin");
        }
    }
}
