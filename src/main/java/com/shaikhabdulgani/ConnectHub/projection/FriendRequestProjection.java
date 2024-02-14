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
public class FriendRequestProjection {

    @Id
    private String friendRequestId;
    private String userId;
    private String username;
    private String email;
    private String profileImageId;
    private Date timestamp;

}
