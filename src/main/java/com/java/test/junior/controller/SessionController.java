package com.java.test.junior.controller;

import com.java.test.junior.model.CredentialsDTO;
import com.java.test.junior.service.session.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Session Handler", description = "Performs session oriented operations")
@RestController
@Validated
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @Operation(summary = "Logs the user in")
    @PostMapping("/login")
    public void login(@Valid @RequestBody CredentialsDTO credentials,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        sessionService.login(credentials, request, response);
    }

    @Operation(summary = "Logs the user out")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        sessionService.logout(request);
    }
}