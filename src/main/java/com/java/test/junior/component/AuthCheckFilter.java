package com.java.test.junior.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.mapper.UserMapper;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.RequestResponse.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthCheckFilter extends OncePerRequestFilter {

    private final UserMapper userMapper;
    private final List<RequestMatcher> permittedEndpointMatchers;
    private final ObjectMapper objectMapper;

    private boolean requiresAuth(HttpServletRequest request) {
        return permittedEndpointMatchers.stream().noneMatch(matcher -> matcher.matches(request));
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(message)
        ));
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
            sendErrorResponse(response, "You must be logged in to access this resource");
            return;
        }

        ExtendedUserDetails userDetails = (ExtendedUserDetails) auth.getPrincipal();
        LocalDateTime authTime = userDetails.getAuthTime();
        LocalDateTime updateTime = userMapper.find(userDetails.getId()).getUpdatedAt();

        if (authTime.isBefore(updateTime)) {
            sendErrorResponse(response, "Your session has expired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}