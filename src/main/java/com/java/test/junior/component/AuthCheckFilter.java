package com.java.test.junior.component;

import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.User.User;
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

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String email = request.getParameter("username");
            if(email == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Credentials cannot be empty");
                return;
            }

            User user = userMapper.findByEmail(email);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
                return;
            }

            if (user.getDeleted()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid email or password");
                return;
            }
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
                filterChain.doFilter(request, response);
                return;
            }

            ExtendedUserDetails userDetails = (ExtendedUserDetails) auth.getPrincipal();
            LocalDateTime authTime = userDetails.getAuthTime();
            LocalDateTime updateTime = userMapper.findById(userDetails.getId()).getUpdatedAt();

            if (authTime.isBefore(updateTime)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication is outdated due to user update");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

