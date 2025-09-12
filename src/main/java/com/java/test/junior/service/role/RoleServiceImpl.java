package com.java.test.junior.service.role;

import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.model.Role;
import com.java.test.junior.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final UserService userService;

    @Override
    public Boolean existsRoleId(Integer id) {
        return roleMapper.exists(id);
    }

    @Override
    public Boolean existsRoleName(String name) {
        return roleMapper.existsName(name);
    }

    @Override
    public Role findRoleById(Integer id) {
        Role role = roleMapper.find(id);
        if (role == null) {
            throw new ResourceNotFoundException("The requested role was not found.");
        }

        return role;
    }

    @Override
    public Role findRoleByName(String name) {
        Role role = roleMapper.findByName(name);
        if (role == null) {
            throw new ResourceNotFoundException("The requested role was not found.");
        }

        return role;
    }

    @Override
    public List<Role> getRoles() {
        return roleMapper.findAll();
    }

    @Override
    public Role createRole(String name) {
        if (existsRoleName(name)) {
            throw new ResourceConflictException("A role with this name already exists.");
        }

        Integer newId = roleMapper.insert(name);
        return findRoleById(newId);
    }

    @Override
    public void deleteRole(Integer roleId) {
        if (roleMapper.delete(roleId) == 0) {
            throw new ResourceNotFoundException("The requested role was not found.");
        }
    }

    @Override
    public Boolean existsUserRole(Long userId, Integer roleId) {
        return roleMapper.existsUserRole(userId, roleId);
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        if (userService.existsUserId(userId)) {
            return roleMapper.findUserRoles(userId);
        }

        throw new ResourceNotFoundException("The requested user was not found.");
    }

    @Override
    public void assignUserRole(Long userId, Integer roleId) {
        if (!userService.existsUserId(userId)) {
            throw new ResourceNotFoundException("The requested user was not found.");
        }

        if(!existsRoleId(roleId)){
            throw new ResourceNotFoundException("The requested role was not found.");
        }

        if (existsUserRole(userId, roleId)) {
            throw new ResourceConflictException("The user role has already been assigned.");
        }

        roleMapper.insertUserRole(userId, roleId);
    }

    @Override
    public void removeUserRole(Long userId, Integer roleId) {
        if (roleMapper.deleteUserRole(userId, roleId) == 0) {
            throw new ResourceNotFoundException("The requested role was not found.");
        }
    }
}