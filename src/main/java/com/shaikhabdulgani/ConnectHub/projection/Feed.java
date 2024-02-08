package com.shaikhabdulgani.ConnectHub.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feed {

    @Id
    private String postId;
    private String imageId;
    private String text;
    private Date createDate;
    private String userId;
    private int totalLikes;
    private int totalComments;
    private String username;
    private String profileImageId;

}
