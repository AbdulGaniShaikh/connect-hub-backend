package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "friendRequests")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FriendRequest {

    @Id
    private String requestId;
    private String sender;
    private String receiver;
    private Date createdOn;

    public FriendRequest(String sender, String receiver){
        this.sender = sender;
        this.receiver = receiver;
        createdOn = new Date();
    }

}
