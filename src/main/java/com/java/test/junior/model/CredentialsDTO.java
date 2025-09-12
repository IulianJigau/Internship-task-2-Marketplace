package com.java.test.junior.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 5, max = 30)
    @Pattern(regexp = "\\S+")
    private String password;


    public String getPassword() {
        return password == null? "DEFAULT" : password;
    }
}
