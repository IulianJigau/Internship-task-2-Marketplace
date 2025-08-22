package com.java.test.junior.component;

import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.User.UserDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
            if (!roleMapper.existsName("Admin")) {
                roleMapper.insert("Admin");
            }
            int roleId = roleMapper.findByName("Admin").getId();

            UserDTO user = new UserDTO();
            user.setEmail(System.getenv("INIT_EMAIL"));
            user.setPassword(System.getenv("INIT_PASSWORD"));

            if (user.getEmail() == null || user.getPassword() == null) {
                return;
            }

            if (!userMapper.existsEmail(user.getEmail())) {
                user.setUsername("Admin");
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userMapper.insert(user);
            }

            Long userId = userMapper.findByEmail(user.getEmail()).getId();

            if (!roleMapper.existsUserRole(userId, roleId)) {
                roleMapper.insertUserRole(userId, roleId);
            }

            logger.info("Admin initialized");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
