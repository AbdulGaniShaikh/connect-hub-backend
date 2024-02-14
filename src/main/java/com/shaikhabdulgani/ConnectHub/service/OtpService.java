package com.shaikhabdulgani.ConnectHub.service;


import com.shaikhabdulgani.ConnectHub.exception.ForbiddenException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.TokenExpiredException;
import com.shaikhabdulgani.ConnectHub.model.Otp;
import com.shaikhabdulgani.ConnectHub.repo.OtpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepo otpRepo;

    public Otp getOtp(String userId) throws NotFoundException {
        Optional<Otp> otp = otpRepo.findById(userId);
        if (otp.isEmpty()){
            throw new NotFoundException("Otp not found for userId: "+userId);
        }
        return otp.get();
    }

    public boolean existsById(String userId){
        return otpRepo.existsById(userId);
    }

    public Otp save(String userId,int otp){
        return otpRepo.save(new Otp(userId,otp,new Date()));
    }

    public Otp generateAndSave(String userId){
        return save(userId,generateOtp());
    }

    private int generateOtp(){
        Random random = new Random();
        return random.nextInt(111111,999999);
    }

    public void verifyIfUserCanAskForOTP(Otp otp) throws ForbiddenException {
        Timestamp currTime = new Timestamp(new Date().getTime()-(Duration.ofSeconds(60).toMillis()));
        if (otp.getExpire().after(currTime)){
            throw new ForbiddenException("too early to ask for reset otp.");
        }
    }

    public void verifyIfTokenExpired(Otp otp) throws TokenExpiredException {
        Timestamp currTime = new Timestamp(new Date().getTime()-(Duration.ofMinutes(5).toMillis()));
        if (currTime.after(otp.getExpire())){
            deleteById(otp.getUserId());
            throw new TokenExpiredException("token expired. please request for new token");
        }
    }

    public void deleteById(String userId) {
        otpRepo.deleteById(userId);
    }
}
