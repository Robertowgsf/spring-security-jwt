package com.robertowgsf.springsecurityjwt.model.request;

import com.robertowgsf.springsecurityjwt.model.validation.UniqueEmail;
import com.robertowgsf.springsecurityjwt.model.validation.UniqueUsername;
import com.robertowgsf.springsecurityjwt.util.RegexValidationConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateAccountRequest {
    @NotEmpty(message = "Username must not be empty!")
    @Pattern(regexp = RegexValidationConstants.USERNAME, message = "Username contains invalid characters!")
    @UniqueUsername(message = "Username already in use! Choose another username.")
    private String username;
    @NotEmpty(message = "Email must not be empty!")
    @Pattern(regexp = RegexValidationConstants.EMAIL, message = "Invalid email!")
    @UniqueEmail(message = "Email already in use! Choose another email.")
    private String email;
    @NotEmpty(message = "Password must not be empty!")
    @Pattern(regexp = RegexValidationConstants.PASSWORD, message = "Password too weak! The password must contain a minimum of eight characters, " +
            "at least one uppercase letter, one lowercase letter, one number and one special character.")
    private String password;
    @NotEmpty(message = "Profile must not be empty!")
    private String profile;
}
