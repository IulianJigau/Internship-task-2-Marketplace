package com.java.test.junior.service.SessionService;

import com.java.test.junior.model.CredentialsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImp implements SessionService {
    private final AuthenticationManager authManager;
    private final SecurityContextRepository securityContextRepository;

    @Override
    public ResponseEntity<?> login(CredentialsDTO credentials, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());

        try {
            Authentication authResult = authManager.authenticate(authToken);

            SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
            SecurityContextHolder.getContext().setAuthentication(authResult);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            return ResponseEntity.ok().build();
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
