package com.java.test.junior.service.SessionService;

import com.java.test.junior.model.CredentialsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface SessionService {
    ResponseEntity<?> login(CredentialsDTO credentials, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logout(HttpServletRequest request);
}
