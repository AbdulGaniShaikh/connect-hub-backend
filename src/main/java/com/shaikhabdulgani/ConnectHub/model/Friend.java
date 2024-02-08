package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("friends")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Friend {

    /**
     * The <b> friendId </b> will serve as a unique identifier for messaging.
     * Both User1 and User2 will subscribe to the chat socket with this
     * identifier to exchange messages between each other.
     * */
    @Id
    private String friendId;
    private String user1;
    private String user2;

    public Friend(String user1,String user2){
        this.user1 = user1;
        this.user2 = user2;
    }

}
