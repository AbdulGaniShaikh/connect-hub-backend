package com.shaikhabdulgani.ConnectHub.dto;

import com.shaikhabdulgani.ConnectHub.util.enums.AuthenticationMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `LoginDto` class represents the data transfer object for user login information.
 * It includes fields for the user's credential (email or username), password, and authentication method.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    /**
     * The user's credential, which can be either an email or username.
     */
    @NotNull(message = "Credential cannot be null")
    @NotBlank(message = "Credential cannot be blank")
    private String credential;

    /**
     * The user's password.
     */
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8,message = "password size cannot be less than 8")
    private String password;

    /**
     * The authentication method used for login (email or username).
     */
    private AuthenticationMethod authMethod;
}
