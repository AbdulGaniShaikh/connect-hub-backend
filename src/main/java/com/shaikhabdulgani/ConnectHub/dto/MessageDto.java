package com.shaikhabdulgani.ConnectHub.dto;

import com.shaikhabdulgani.ConnectHub.util.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    @NotNull(message = "chatId cannot be null")
    @NotBlank(message = "chatId cannot be blank")
    private String chatId;

    @NotNull(message = "senderId cannot be null")
    @NotBlank(message = "senderId cannot be blank")
    private String senderId;

    @NotNull(message = "receiverId cannot be null")
    @NotBlank(message = "receiverId cannot be blank")
    private String receiverId;

    @NotNull(message = "message cannot be null")
    @NotBlank(message = "message cannot be blank")
    private String message;

    @NotNull(message = "messageType cannot be null")
    @NotBlank(message = "messageType cannot be blank")
    private MessageType messageType;

}
