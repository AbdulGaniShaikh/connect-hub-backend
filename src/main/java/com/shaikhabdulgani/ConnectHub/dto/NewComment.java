package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewComment {

    @NotNull(message = "userId cannot be null")
    @NotBlank(message = "userId cannot be blank")
    String userId;

    @NotNull(message = "comment cannot be null")
    @NotBlank(message = "comment cannot be blank")
    String comment;

}
