package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotNull(message = "oldPassword cannot be null")
    @NotBlank(message = "oldPassword cannot be blank")
    @Size(min = 8, max = 40)
    private String oldPassword;

    @NotNull(message = "newPassword cannot be null")
    @NotBlank(message = "newPassword cannot be blank")
    @Size(min = 8, max = 40)
    private String newPassword;

}
