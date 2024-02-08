package com.shaikhabdulgani.ConnectHub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The `FriendRequestData` class represents the data transfer object for a new friend request.
 * It includes fields for the sender's ID and the receiver's ID.
 */
@Data
public class FriendRequestData {

    /**
     * The userId of the user sending the friend request.
     */
    @NotNull(message = "Sender ID cannot be null")
    @NotBlank(message = "Sender ID cannot be blank")
    private String senderId;

    /**
     * The userId of the user receiving the friend request.
     */
    @NotNull(message = "Receiver ID cannot be null")
    @NotBlank(message = "Receiver ID cannot be blank")
    private String receiverId;
}
