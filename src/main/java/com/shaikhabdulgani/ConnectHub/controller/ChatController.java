package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.MessageDto;
import com.shaikhabdulgani.ConnectHub.model.Message;
import com.shaikhabdulgani.ConnectHub.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Controller class for handling chat-related operations.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {


    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Handles sending private messages.
     *
     * @param messageDto The MessageDto containing message information
     * @return The sent message
     */
    @MessageMapping("/private-message")
    public Message sendPrivateMessage(@Payload @Valid MessageDto messageDto){
        messageDto.setChatId(messageService.getChatId(messageDto.getSenderId(),messageDto.getReceiverId()));
        Message message = messageService.saveMessage(messageDto);

        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiverId(),"/private",message);
        if (!messageDto.getReceiverId().equals(message.getSenderId())) {
            simpMessagingTemplate.convertAndSendToUser(messageDto.getSenderId(), "/private", message);
        }
        return message;
    }
}
