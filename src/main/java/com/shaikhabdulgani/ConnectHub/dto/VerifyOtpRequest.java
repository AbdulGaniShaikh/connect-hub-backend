package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Represents a request to verify an OTP and set a new password.
 * This class encapsulates the new password and the OTP for verification.
 */
@Data
public class VerifyOtpRequest {

    /**
     * The new password to be set.
     * Cannot be null or blank, and must be at least 8 characters long.
     */
    @NotBlank(message = "Password cannot be null")
    @Size(min = 8,message = "Password size cannot be less than 8")
    private String newPassword;

    /**
     * The OTP (One-Time Password) for verification.
     * Must be between 111111 and 999999.
     */
    @Min(message = "Invalid otp",value = 111111)
    @Max(message = "Invalid otp",value = 999999)
    private int otp;

}
