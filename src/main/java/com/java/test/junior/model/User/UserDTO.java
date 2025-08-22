package com.java.test.junior.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 3, max = 30)
    @Pattern(regexp = "\\S+")
    private String username;
    @NotBlank
    @Size(min = 5, max = 30)
    @Pattern(regexp = "\\S+")
    private String password;
}
