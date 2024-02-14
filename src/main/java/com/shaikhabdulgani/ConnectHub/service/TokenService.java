package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.ForbiddenException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.TokenExpiredException;
import com.shaikhabdulgani.ConnectHub.model.Token;
import com.shaikhabdulgani.ConnectHub.repo.TokenRepo;
import com.shaikhabdulgani.ConnectHub.util.UniqueId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepo repo;

    public Token getToken(String userId) throws NotFoundException {
        Optional<Token> token = repo.findById(userId);
        if (token.isEmpty()){
            throw new NotFoundException("Token not found for userId "+userId);
        }
        return token.get();
    }

    public Token save(String userId){
        Token token = Token.builder()
                .date(new Date())
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

    public void isTokenValid(Token token, String userId) throws TokenExpiredException {
        Date expiry = new Date(token.getDate().getTime()+Duration.ofDays(1).toMillis());
        Date currTime = new Date();
        if (currTime.after(expiry)) {
            throw new TokenExpiredException("token expired. please request for new token");
        }
    }

    public void checkIfUserCanAskForToken(Token token) throws ForbiddenException {
        Date expiry = new Date(token.getDate().getTime()+Duration.ofMinutes(5).toMillis());
        Date currTime = new Date();
        if (currTime.before(expiry)) {
            throw new ForbiddenException("Cannot send token. You need to wait for 5 min before asking for a new token");
        }
    }

    public void deleteToken(String userId) {
        repo.deleteById(userId);
    }
}
