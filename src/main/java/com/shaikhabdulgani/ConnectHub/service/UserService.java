package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.dto.*;
import com.shaikhabdulgani.ConnectHub.exception.*;
import com.shaikhabdulgani.ConnectHub.model.Otp;
import com.shaikhabdulgani.ConnectHub.model.Token;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.util.DefaultDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BasicUserService basicUserService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final ImageService imageService;
    private final OtpService otpService;

    public User registerUser(SignUpDto req) throws AlreadyExistsException {

        basicUserService.checkIfUserAlreadyExists(req.getUsername(),req.getEmail());
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail().toLowerCase())
                .password(req.getPassword())
                .totalFriends(0)
                .totalPost(0)
                .description(DefaultDescription.getRandomDescription())
                .lastSeen(new Date())
                .build();

        user = basicUserService.updateWithPassword(user);
        user.clearPassword();
        generateAndSendToken(user.getUserId(),req.getEmail());
        return user;
    }

    public String resendVerificationEmail(String email) throws NotFoundException, ForbiddenException {
        User user = basicUserService.getByEmail(email);
        try {
            Token token = tokenService.getToken(user.getUserId());
            tokenService.checkIfUserCanAskForToken(token);
        }catch (NotFoundException ignored){

        }catch (ForbiddenException e){
            throw new ForbiddenException(e.getMessage());
        }

        //override old token, generate new token and send it to user
        generateAndSendToken(user.getUserId(),user.getEmail());

        return "Verification link was sent successfully";
    }

    private void generateAndSendToken(String userId, String email) {

        Token token = tokenService.save(userId);
        emailService.sendVerificationLink(email, token.getToken());

    }

    public String updateDescription(String userId, UpdateDescription description, String token) throws NotFoundException, UnauthorizedAccessException {

        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        basicUserService.update(userId,"description",description.getDescription());
        return "Description was updated";

    }

    public String updateProfile(String userId, MultipartFile file, String token) throws NotFoundException, UnauthorizedAccessException, IOException {

        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        String imageId = imageService.uploadImage(file, userId);
        basicUserService.update(userId,"profileImageId",imageId);
        return imageId;

    }

    public String updateCover(String userId, MultipartFile file, String token) throws NotFoundException, UnauthorizedAccessException, IOException {

        User user = basicUserService.getById(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        String imageId = imageService.uploadImage(file, userId);
        basicUserService.update(userId,"coverImageId",imageId);
        return imageId;

    }

    public String verifyUser(String email, String tokenStr) throws NotFoundException, TokenExpiredException {

        User user = basicUserService.getByEmail(email);
        if(user.isVerified()){
            return "Verified";
        }
        Token token = tokenService.getTokenAndVerify(user.getUserId(),tokenStr);
        tokenService.isTokenValid(token,user.getUserId());
        tokenService.deleteToken(user.getUserId());
        basicUserService.setUserIsVerified(user.getUserId());
        return "Verification Success. Your account is verified now.";

    }

    public boolean sendResetOTP(String email) throws NotFoundException, ForbiddenException {
        User user = basicUserService.getByEmail(email);

        Otp otp;
        if (otpService.existsById(user.getUserId())){
            otp = otpService.getOtp(user.getUserId());
            otpService.verifyIfUserCanAskForOTP(otp);
        }

        otp = otpService.generateAndSave(user.getUserId());
        emailService.sendResetOtp(user.getEmail(),otp.getOtp());

        return true;
    }

    public boolean verifyOTP(String email, VerifyOtpRequest req) throws NotFoundException, UnauthorizedAccessException, TokenExpiredException {
        User user = basicUserService.getByEmail(email);
        Otp otp = otpService.getOtp(user.getUserId());

        if (otp.getOtp()!=req.getOtp()){
            throw new UnauthorizedAccessException("Wrong OTP entered.");
        }

        otpService.verifyIfTokenExpired(otp);
        otpService.deleteById(user.getUserId());

        user.setPassword(req.getNewPassword());
        basicUserService.updateWithPassword(user);
        return true;
    }

    public boolean changePassword(String userId, ChangePasswordRequest passwordRequest, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getUserWithPassword(userId);
        basicUserService.checkIfUserIsAuthorized(user,token);
        basicUserService.checkIfPasswordMatches(user,passwordRequest.getOldPassword());

        user.setPassword(passwordRequest.getNewPassword());
        basicUserService.updateWithPassword(user);
        return true;
    }
}
