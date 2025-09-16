package com.java.test.junior.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class ExtendedUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private LocalDateTime authTime;

    public ExtendedUserDetails(Long id,
                               String username,
                               String email,
                               String password,
                               Collection<? extends GrantedAuthority> authorities,
                               LocalDateTime authTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.authTime = authTime;
    }
}
