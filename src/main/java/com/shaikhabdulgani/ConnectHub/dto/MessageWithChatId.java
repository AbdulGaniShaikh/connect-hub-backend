package com.shaikhabdulgani.ConnectHub.dto;

import com.shaikhabdulgani.ConnectHub.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * Represents a collection of messages associated with a chat.
 * This class encapsulates the chat ID and a page of messages.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageWithChatId {

    /**
     * The ID of the chat associated with the messages.
     */
    private String chatId;

    /**
     * A page of messages.
     */
    private Page<Message> messages;

}
