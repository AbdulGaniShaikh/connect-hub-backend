package com.shaikhabdulgani.ConnectHub.projection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class InboxProjection {

    private String userId;
    private String username;
    private String email;
    private String profileImageId;
    private Date lastSeen;
    private String lastMessage;
    private long unreadMessageCount;
    private Date date;
    private boolean isPost;
    private String senderId;

}
