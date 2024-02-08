package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.MessageDto;
import com.shaikhabdulgani.ConnectHub.model.Message;
import com.shaikhabdulgani.ConnectHub.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // Mapped as /app/chat/{userId}
    @MessageMapping("/chat/{userId}")
    @SendTo("messages/{userId}")//all user subscribed to this socket will receive message
    public MessageDto sendPublicMessage(@Payload @Valid MessageDto messageDto) {
        chatService.saveMessage(messageDto);
        return messageDto;
    }

    @MessageMapping("/private-message")
    public MessageDto sendPrivateMessage(@Payload @Valid MessageDto messageDto){
        chatService.saveMessage(messageDto);
        simpMessagingTemplate.convertAndSendToUser(messageDto.getChatId(),"/private",messageDto);
        return messageDto;
    }
}
