package com.java.test.junior.service.Role;

import com.java.test.junior.exception.DatabaseFailException;
import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    @Override
    public List<Role> getRoles() throws DatabaseFailException {
        return roleMapper.findAll();
    }

    @Override
    public void createRole(String name) throws DatabaseFailException {
        if (roleMapper.existsName(name)) {
            throw new ResourceConflictException("A role with this name already exists.");
        }

        roleMapper.insert(name);
    }

    @Override
    public void deleteRole(Integer roleId) throws DatabaseFailException {
        int result = roleMapper.delete(roleId);
        if (result == 0) {
            throw new ResourceNotFoundException("The requested role was not found.");
        }
    }

    @Override
    public List<String> getUserRoles(Long userId) throws DatabaseFailException {
        boolean exists = userMapper.exists(userId);
        if (!exists) {
            throw new ResourceNotFoundException("The requested user was not found.");
        }

        return roleMapper.findUserRoles(userId);
    }

    @Override
    public void assignUserRole(Long userId, Integer roleId) throws DatabaseFailException {
        boolean exists = userMapper.exists(userId) && roleMapper.exists(roleId);
        if (!exists) {
            throw new ResourceNotFoundException("The requested user or role were not found.");
        }

        if (roleMapper.existsUserRole(userId, roleId)) {
            throw new ResourceConflictException("The user role has already been assigned.");
        }

        roleMapper.insertUserRole(userId, roleId);
    }

    @Override
    public void removeUserRole(Long userId, Integer roleId) throws DatabaseFailException {
        int result = roleMapper.deleteUserRole(userId, roleId);
        if (result == 0) {
            throw new ResourceNotFoundException("The requested role was not found.");
        }
    }
}
