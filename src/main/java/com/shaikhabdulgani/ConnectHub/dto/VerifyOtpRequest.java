package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotBlank(message = "new password cannot be null")
    private String newPassword;

    @NotBlank(message = "otp cannot be null")
    @Min(message = "invalid otp",value = 111111)
    @Max(message = "invalid otp",value = 999999)
    private int otp;

}
