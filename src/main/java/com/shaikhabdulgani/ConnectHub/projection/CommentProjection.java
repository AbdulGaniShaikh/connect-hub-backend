package com.shaikhabdulgani.ConnectHub.projection;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class CommentProjection {

    private String comment;
    private String username;
    private String userId;
    private String profileImageId;
    private Date date;

}
