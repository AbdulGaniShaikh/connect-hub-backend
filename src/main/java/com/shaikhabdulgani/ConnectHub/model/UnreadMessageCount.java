package com.shaikhabdulgani.ConnectHub.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "unreadMessageCount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnreadMessageCount {

    @Id
    private String counterId;
    private String senderId;
    private String receiverId;
    private long count;

}
