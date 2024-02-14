package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents an update to the user description.
 * This class encapsulates the new user description.
 */
@Data
public class UpdateDescription {

    /**
     * The new user description.
     * Cannot be null or blank.
     */
    @NotNull(message = "description cannot be null")
    @NotBlank(message = "description cannot be blank")
    private String description;

}