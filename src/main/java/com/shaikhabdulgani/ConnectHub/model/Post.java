package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Post {

    @Id
    private String postId;
    private String imageId;
    private String text;
    private Date createDate;
    private String userId;
    private int totalLikes;
    private int totalComments;

}
