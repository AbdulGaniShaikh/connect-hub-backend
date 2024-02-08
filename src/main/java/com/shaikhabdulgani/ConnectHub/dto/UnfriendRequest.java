package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The `UnfriendRequest` class represents the data transfer object for unfriending a user.
 * It includes fields for the ID of the friend to unfriend and the ID of the user making the request.
 */
@Data
public class UnfriendRequest {

    /**
     * The ID of the friend to unfriend.
     */
    @NotNull(message = "Friend to unfriend cannot be null")
    @NotBlank(message = "Friend to unfriend cannot be blank")
    private String friendToUnfriend;

    /**
     * The ID of the user making the unfriend request.
     */
    @NotNull(message = "Asker cannot be null")
    @NotBlank(message = "Asker cannot be blank")
    private String asker;
}
