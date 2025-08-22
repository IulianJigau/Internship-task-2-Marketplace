package com.java.test.junior.component;

import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthCheckFilter extends OncePerRequestFilter {

    private final UserMapper userMapper;
    private final PermitAllConfig permitAllConfig;

    private boolean requiresAuth(HttpServletRequest request) {
        return permitAllConfig.getMatchers().stream().noneMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (!requiresAuth(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ExtendedUserDetails userDetails = (ExtendedUserDetails) auth.getPrincipal();
        LocalDateTime authTime = userDetails.getAuthTime();
        LocalDateTime updateTime = userMapper.find(userDetails.getId()).getUpdatedAt();

        if (authTime.isBefore(updateTime)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

