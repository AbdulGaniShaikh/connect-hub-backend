package com.shaikhabdulgani.ConnectHub.service;


import com.shaikhabdulgani.ConnectHub.dto.MessageDto;
import com.shaikhabdulgani.ConnectHub.dto.MessageWithChatId;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.Message;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.projection.InboxProjection;
import com.shaikhabdulgani.ConnectHub.repo.ChatRepo;
import com.shaikhabdulgani.ConnectHub.util.CustomPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final ChatRepo messageRepository;
    private final UnreadMessageCountService countService;
    private final BasicUserService basicUserService;
    private final MongoTemplate mongoTemplate;

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

        countService.increaseCount(messageDto.getSenderId(), messageDto.getReceiverId());

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
        if (pageNumber==0){
            countService.clearCount(user2,user1);
        }
        return new MessageWithChatId(chatId,messageRepository.findByChatId(getChatId(user1,user2), PageRequest.of(pageNumber,pageSize, Sort.by("date").descending())));
    }

    public long countOfUsersMessaged(String userId){
        return 0;
    }

    public CustomPage<InboxProjection> getInbox(String userId,int pageNumber,int pageSize,String token) throws NotFoundException, UnauthorizedAccessException {

        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);

        List<InboxProjection> result = messageRepository.findLastMessagesByUser(userId,pageNumber*pageSize,pageSize);

        result.forEach(inboxProjection -> {
            inboxProjection.setUnreadMessageCount(countService.totalUnreadMessagesBetweenUsers(userId,inboxProjection.getUserId()));
        });

        CustomPage<InboxProjection> pagedResult = new CustomPage<>();
        pagedResult.setPageNumber(pageNumber);
        pagedResult.setPageSize(pageSize);
        pagedResult.setSize(result.size());
        pagedResult.setTotalElements((int)countOfUsersMessaged(userId));
        pagedResult.setContent(result);

        return pagedResult;

    }
}

