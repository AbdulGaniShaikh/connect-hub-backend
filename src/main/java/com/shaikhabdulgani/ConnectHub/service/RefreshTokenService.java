package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.model.RefreshToken;
import com.shaikhabdulgani.ConnectHub.repo.RefreshTokenRepo;
import com.shaikhabdulgani.ConnectHub.util.UniqueId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepo repo;
    private static final long REFRESH_TOKEN_VALIDITY = Duration.ofDays(10).toMillis();

    public RefreshToken getToken(String tokenStr) throws NotFoundException {
        Optional<RefreshToken> token = repo.findById(tokenStr);
        if (token.isEmpty()){
            throw new NotFoundException("Refresh token "+tokenStr+" not found!");
        }
        return token.get();
    }

    public RefreshToken generateRefreshToken(String userId){
        return repo.save(new RefreshToken(userId,new Date(System.currentTimeMillis()+REFRESH_TOKEN_VALIDITY)));
    }

    public boolean validateToken(String userId,String tokenStr) throws NotFoundException {
        RefreshToken token = getToken(tokenStr);
        return (token.getUserId().equals(userId) && new Date().before(token.getExpiry()));
    }

    public void delete(String refreshToken) {
        repo.deleteById(refreshToken);
    }
}
