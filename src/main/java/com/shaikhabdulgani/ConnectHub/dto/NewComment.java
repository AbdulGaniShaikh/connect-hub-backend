package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents a new comment DTO.
 * This class encapsulates information about a user's comment.
 */
@Data
public class NewComment {

    /**
     * The ID of the user who posted the comment.
     * Cannot be null or blank.
     */
    @NotNull(message = "userId cannot be null")
    @NotBlank(message = "userId cannot be blank")
    private String userId;

    /**
     * The content of the comment.
     * Cannot be null or blank.
     */
    @NotNull(message = "comment cannot be null")
    @NotBlank(message = "comment cannot be blank")
    private String comment;

}