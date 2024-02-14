package com.shaikhabdulgani.ConnectHub.projection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserProjection {
    @Id
    private String userId;
    private String username;
    private String profileImageId;
    private String email;
}
