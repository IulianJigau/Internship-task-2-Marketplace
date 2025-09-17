package com.java.test.junior.component.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.component.RoleChecker;
import com.java.test.junior.model.ExtendedUserDetails;
import com.java.test.junior.model.actionHistory.ActionHistoryDTO;
import com.java.test.junior.model.response.ErrorResponse;
import com.java.test.junior.service.actionHistory.ActionHistoryService;
import com.java.test.junior.service.user.UserService;
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

    private final UserService userService;
    private final RoleChecker roleChecker;
    private final ActionHistoryService actionHistoryService;
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
    protected void doFilterInternal(@NonNull HttpServletRequest request,
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
        LocalDateTime updateTime = userService.getUserById(userDetails.getId()).getUpdatedAt();

        if (authTime.isBefore(updateTime)) {
            sendErrorResponse(response, "Your session has expired");
            return;
        }

        if (!roleChecker.hasAdminRole(userDetails)) {
            filterChain.doFilter(request, response);
        }

        if ("DELETE".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            ActionHistoryDTO actionHistory = new ActionHistoryDTO(
                    userDetails.getId(),
                    request.getMethod(),
                    request.getRequestURI()
            );
            actionHistoryService.createActionHistory(actionHistory);
        }

        filterChain.doFilter(request, response);
    }
}