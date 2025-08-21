package com.java.test.junior.controller;

import com.java.test.junior.model.CredentialsDTO;
import com.java.test.junior.service.SessionService.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody CredentialsDTO credentials,
            HttpServletRequest request,
            HttpServletResponse response) {
        return sessionService.login(credentials, request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request) {
        return sessionService.logout(request);
    }
}
