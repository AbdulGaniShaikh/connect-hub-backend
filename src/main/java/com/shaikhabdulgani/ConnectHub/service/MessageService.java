package com.shaikhabdulgani.ConnectHub.service;


import com.shaikhabdulgani.ConnectHub.dto.MessageDto;
import com.shaikhabdulgani.ConnectHub.dto.MessageWithChatId;
import com.shaikhabdulgani.ConnectHub.model.Message;
import com.shaikhabdulgani.ConnectHub.repo.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {

    @Autowired
    private ChatRepo messageRepository;

    public Message saveMessage(MessageDto messageDto) {

        Message message = Message.builder()
                .chatId(messageDto.getChatId())
                .senderId(messageDto.getSenderId())
                .receiverId(messageDto.getReceiverId())
                .message(messageDto.getMessage())
                .isPost(messageDto.isPost())
                .date(new Date())
                .seen(false)
                .build();

        return messageRepository.save(message);
    }

    public String getChatId(String user1,String user2){
        String chatId;
        if (user1.compareTo(user2)<0){
            chatId = user1+user2;
        }else{
            chatId = user2+user1;
        }
        return chatId;
    }

    public MessageWithChatId getAllMessages(String user1, String user2, int pageNumber, int pageSize) {
        String chatId = getChatId(user1,user2);
        return new MessageWithChatId(chatId,messageRepository.findByChatId(getChatId(user1,user2), PageRequest.of(pageNumber,pageSize, Sort.by("date").descending())));
    }
}

