package com.shaikhabdulgani.ConnectHub.projection;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserInfo {
    private String username;
    private String userId;
    private String profileImageId;
}
