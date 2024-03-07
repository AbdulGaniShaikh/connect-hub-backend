package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "refreshTokens")
public class RefreshToken {

    @Id
    private String refreshToken;
    private String userId;
    private Date expiry;

    public RefreshToken(String userId, Date expiry) {
        this.userId = userId;
        this.expiry = expiry;
    }
}
