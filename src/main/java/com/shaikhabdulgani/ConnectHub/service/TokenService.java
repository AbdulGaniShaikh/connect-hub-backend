package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.TokenExpiredException;
import com.shaikhabdulgani.ConnectHub.model.Token;
import com.shaikhabdulgani.ConnectHub.repo.TokenRepo;
import com.shaikhabdulgani.ConnectHub.util.UniqueId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepo repo;

    public Token getToken(String userId) throws NotFoundException {
        Optional<Token> emailVerification = repo.findById(userId);
        if (emailVerification.isEmpty()){
            throw new NotFoundException("Token not found for userId "+userId);
        }
        return emailVerification.get();
    }

    public Token save(String userId){
        Token token = Token.builder()
                .expiry(new Date())
                .userId(userId)
                .token(UniqueId.generateToken())
                .build();

        return repo.save(token);
    }

    public Token getTokenAndVerify(String userId, String tokenStr) throws NotFoundException {
        Token token = getToken(userId);
        if (!token.getToken().equals(tokenStr)) {
            throw new NotFoundException("token doesn't match with the sent token");
        }
        return token;
    }

    public void checkTokensValidityAndDeleteToken(Token token, String userId) throws TokenExpiredException {
        Date expiry = token.getExpiry();
        Timestamp currTime = new Timestamp(new Date().getTime() - Duration.ofDays(1).toMillis());
        if (expiry.before(currTime)) {
            repo.deleteById(userId);
            throw new TokenExpiredException("token expired. please request for new token");
        }
    }
}
