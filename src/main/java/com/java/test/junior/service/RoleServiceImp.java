package com.java.test.junior.service;

import com.java.test.junior.mapper.RoleMapper;
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

    @Override
    public ResponseEntity<List<Role>> getRoles() {
        try {
            List<Role> roles = roleMapper.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> createRole(String name) {
        try {
            roleMapper.insert(name);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteRole(Long id) {
        try {
            int result = roleMapper.delete(id);
            return (result > 0)
                    ? ResponseEntity.ok().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
