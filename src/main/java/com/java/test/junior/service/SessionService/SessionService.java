package com.java.test.junior.service.SessionService;

import com.java.test.junior.model.CredentialsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SessionService {
    void login(CredentialsDTO credentials, HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request);
}
