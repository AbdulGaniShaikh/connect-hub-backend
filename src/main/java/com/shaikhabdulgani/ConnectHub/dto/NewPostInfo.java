package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The `NewPostInfo` class represents the data transfer object for creating a new post.
 * It includes fields for the user ID and the text content of the post.
 */
@Data
public class NewPostInfo {

    /**
     * The userId of the user creating the post.
     */
    @NotNull(message = "userId cannot be null")
    @NotBlank(message = "userId cannot be blank")
    private String userId;

    /**
     * The text content of the post.
     */
    private String text;
}
