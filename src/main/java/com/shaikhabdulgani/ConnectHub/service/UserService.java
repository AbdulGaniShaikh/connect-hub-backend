package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.dto.*;
import com.shaikhabdulgani.ConnectHub.exception.*;
import com.shaikhabdulgani.ConnectHub.model.Otp;
import com.shaikhabdulgani.ConnectHub.model.Token;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.util.enums.Relation;
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
                .lastSeen(new Date())
                .build();

        user = basicUserService.updateWithPassword(user);
        user.clearPassword();
        generateAndSendToken(user.getUserId(),req.getEmail());
        return user;
    }

    private void generateAndSendToken(String userId, String email) {

        Token token = tokenService.save(userId);
        emailService.sendVerificationLink(userId,email, token.getToken());

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

    public String verifyUser(String userId, String tokenStr) throws AlreadyExistsException, NotFoundException, TokenExpiredException {

        User user = basicUserService.getById(userId);
        if(user.isVerified()){
            throw new AlreadyExistsException("User is already verified.");
        }
        Token token = tokenService.getTokenAndVerify(userId,tokenStr);
        tokenService.checkTokensValidityAndDeleteToken(token,userId);
        basicUserService.setUserIsVerified(userId);
        return "Verification Success. Your account is verified now.";

    }

    public boolean sendResetOTP(String userId) throws NotFoundException, ForbiddenException {
        User user = basicUserService.getById(userId);

        Otp otp = otpService.getOtp(userId);
        otpService.verifyIfUserCanAskForOTP(otp);

        otp = otpService.generateAndSave(userId);

        emailService.sendResetOtp(user.getEmail(),otp.getOtp());
        return true;
    }

    public boolean verifyOTP(String userId, VerifyOtpRequest req) throws NotFoundException, UnauthorizedAccessException, TokenExpiredException {
        User user = basicUserService.getById(userId);

        Otp otp = otpService.getOtp(userId);

        if (otp.getOtp()!=req.getOtp()){
            throw new UnauthorizedAccessException("wrong otp entered.");
        }

        otpService.verifyIfTokenExpired(otp);
        otpService.deleteById(userId);

        user.setPassword(req.getNewPassword());
        basicUserService.updateWithPassword(user);
        return true;
    }

    public boolean changePassword(String userId, ChangePasswordRequest passwordRequest, String token) throws NotFoundException, UnauthorizedAccessException {
        User user = basicUserService.getUserWithPassword(userId);
        basicUserService.checkIfPasswordMatches(user,passwordRequest.getOldPassword());

        user.setPassword(passwordRequest.getNewPassword());
        basicUserService.updateWithPassword(user);
        return true;
    }
}
