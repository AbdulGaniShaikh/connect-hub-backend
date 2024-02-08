package com.shaikhabdulgani.ConnectHub.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "likes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Like {

    @Id
    private String likeId;
    private String postId;
    private String userId;

    @Override
    public String toString() {
        return "Like{" +
                "likeId='" + likeId + '\'' +
                ", postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
