package com.java.test.junior.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialsDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
