package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.MessageWithChatId;
import com.shaikhabdulgani.ConnectHub.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling message-related operations.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * Retrieves all messages between two users.
     *
     * @param user1 The ID of the first user
     * @param user2 The ID of the second user
     * @param pageNumber The page number for pagination (default value is 0)
     * @param pageSize The size of each page for pagination (default value is 10)
     * @return A MessageWithChatId object containing chat ID and all messages between the specified users
     */
    @GetMapping("/api/chat/{user1}/{user2}")
    public MessageWithChatId getAllMessages(
            @PathVariable String user1,
            @PathVariable String user2,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ){
        return messageService.getAllMessages(user1,user2,pageNumber,pageSize);
    }

}
