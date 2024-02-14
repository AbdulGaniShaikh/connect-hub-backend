package com.shaikhabdulgani.ConnectHub.dto;

import com.shaikhabdulgani.ConnectHub.util.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents a message data transfer object.
 * This class encapsulates information about a message, including sender and receiver IDs,
 * the message content, and whether it pertains to a post.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    /**
     * The ID of the chat associated with the message.
     */
    private String chatId;

    /**
     * The ID of the message sender.
     * Cannot be null or blank.
     */
    @NotNull(message = "senderId cannot be null")
    @NotBlank(message = "senderId cannot be blank")
    private String senderId;

    /**
     * The ID of the message receiver.
     * Cannot be null or blank.
     */
    @NotNull(message = "receiverId cannot be null")
    @NotBlank(message = "receiverId cannot be blank")
    private String receiverId;

    /**
     * The content of the message.
     * Cannot be null or blank.
     */
    @NotNull(message = "message cannot be null")
    @NotBlank(message = "message cannot be blank")
    private String message;

    /**
     * Indicates whether the message a contains post ID.
     */
    private boolean isPost;

}
