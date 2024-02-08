package com.shaikhabdulgani.ConnectHub.service;


import com.shaikhabdulgani.ConnectHub.dto.MessageDto;
import com.shaikhabdulgani.ConnectHub.model.Message;
import com.shaikhabdulgani.ConnectHub.repo.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ChatService {

    @Autowired
    private ChatRepo messageRepository;

    public Message saveMessage(MessageDto messageDto) {
        Message message = Message.builder()
                .chatId(messageDto.getChatId())
                .senderId(messageDto.getSenderId())
                .receiverId(messageDto.getReceiverId())
                .message(messageDto.getMessage())
                .messageType(messageDto.getMessageType())
                .date(new Date())
                .seen(false)
                .build();

        return messageRepository.save(message);
    }
}

