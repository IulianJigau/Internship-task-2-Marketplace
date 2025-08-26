package com.java.test.junior.service;

import com.java.test.junior.mapper.RoleMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email);
        if (user == null || user.getIsDeleted()) {
            throw new UsernameNotFoundException(email);
        }

        List<String> roles = roleMapper.findUserRoles(user.getId());

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        LocalDateTime authTime = LocalDateTime.now();

        return new ExtendedUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                authTime
        );
    }
}
