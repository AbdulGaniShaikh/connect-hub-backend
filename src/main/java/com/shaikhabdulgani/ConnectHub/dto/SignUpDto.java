package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `SignUpDto` class represents the data transfer object for user sign-up information.
 * It includes fields for the user's email, username, first name, last name, and password.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    /**
     * The user's email address.
     */
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    /**
     * The user's chosen username.
     */
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 15,message = "username size cannot be less than 3 and more than 15")
    private String username;

    /**
     * The user's chosen password.
     */
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8,message = "password size cannot be less than 8")
    private String password;
}
