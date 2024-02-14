package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * Represents a request to change the password.
 * This class encapsulates the old and new passwords for the password change operation.
 */
@Data
public class ChangePasswordRequest {

    /**
     * The old password.
     * Cannot be null or blank and must be between 8 and 40 characters long.
     */
    @NotNull(message = "old password cannot be null")
    @NotBlank(message = "old password cannot be blank")
    @Size(min = 8, max = 40)
    private String oldPassword;

    /**
     * The new password.
     * Cannot be null or blank and must be between 8 and 40 characters long.
     */
    @NotNull(message = "new password cannot be null")
    @NotBlank(message = "new password cannot be blank")
    @Size(min = 8, max = 40)
    private String newPassword;

}
