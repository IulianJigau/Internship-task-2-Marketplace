package com.java.test.junior.service.RoleService;

import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImp.class);

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<?> getRoles() {
        try {
            List<Role> roles = roleMapper.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> createRole(String name) {
        try {
            if (roleMapper.existsName(name)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            roleMapper.insert(name);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> deleteRole(Integer roleId) {
        try {
            int result = roleMapper.delete(roleId);
            return (result > 0)
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> getUserRoles(Long userId) {
        try {
            boolean exists = userMapper.exists(userId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<String> roles = roleMapper.findUserRoles(userId);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> assignUserRole(Long userId, Integer roleId) {
        try {
            boolean exists = userMapper.exists(userId) && roleMapper.exists(roleId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (roleMapper.existsUserRole(userId, roleId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            roleMapper.insertUserRole(userId, roleId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> removeUserRole(Long userId, Integer roleId) {
        try {
            int result = roleMapper.deleteUserRole(userId, roleId);
            return (result > 0) ?
                    ResponseEntity.ok().build() :
                    ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
