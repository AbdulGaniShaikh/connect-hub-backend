package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    @Id
    private String messageId;
    private String chatId;
    private String senderId;
    private String receiverId;
    private String message;
    private Date date;
    private boolean seen;
    private boolean isPost;

}
