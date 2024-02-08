package com.shaikhabdulgani.ConnectHub.projection;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class CommentDto {

    private String comment;
    private UserInfo user;

}
