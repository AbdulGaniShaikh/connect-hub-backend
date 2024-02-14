package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.ApiResponse;
import com.shaikhabdulgani.ConnectHub.dto.LoginDto;
import com.shaikhabdulgani.ConnectHub.dto.SignUpDto;
import com.shaikhabdulgani.ConnectHub.dto.VerifyOtpRequest;
import com.shaikhabdulgani.ConnectHub.exception.*;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.UserService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class responsible for handling authentication-related endpoints.
 * This includes user login, sign-up, and logout functionality.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final CookieService cookieService;
    private final BasicUserService basicUserService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Handles the user login process by authenticating the user's credentials.
     * If authentication is successful, generates and adds JWT and user ID cookies
     * to the response, clearing the user's password before returning the user information.
     *
     * @param response   The HttpServletResponse to add cookies to.
     * @param loginDto   The LoginDto containing user credentials for login.
     * @return The authenticated User information with sensitive data cleared.
     * @throws NotFoundException           If the user is not found.
     * @throws UnauthorizedAccessException If the provided username/email and password doesn't match the saved user info.
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public User login(
            HttpServletResponse response,
            @RequestBody @Valid LoginDto loginDto
            ) throws NotFoundException, UnauthorizedAccessException {

        User user = basicUserService.getUser(loginDto.getCredential(),loginDto.getAuthMethod());
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),loginDto.getPassword()
                        )
                );
        if (!authentication.isAuthenticated()){
            throw new UnauthorizedAccessException("user is not authenticated");
        }
        response.addCookie(cookieService.generateJwtCookie(user.getUsername()));
        response.addCookie(cookieService.generateUserIdCookie(user.getUserId()));
        user.clearPassword();
        return user;
    }

    /**
     * Handles user registration by processing the provided SignUpDto.
     * Registers the user, generates and adds JWT and user ID cookies to the response,
     * clearing the user's password before returning the user information.
     *
     * @param response   The HttpServletResponse to add cookies to.
     * @param signUpDto  The SignUpDto containing user information for registration.
     * @return The registered User information with sensitive data cleared.
     * @throws AlreadyExistsException If the username or email already exists in the database.
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public User signUp(
            HttpServletResponse response,
            @RequestBody @Valid SignUpDto signUpDto
    ) throws AlreadyExistsException{
        User user = userService.registerUser(signUpDto);

        response.addCookie(cookieService.generateJwtCookie(user.getUsername()));
        response.addCookie(cookieService.generateUserIdCookie(user.getUserId()));

        return user;
    }

    /**
     * Handles user logout by adding cookies with quick expiry and unsetting the
     * token and userId cookie value in the response.
     *
     * @param response The HttpServletResponse to add cookies with expiry as now and unsetting the cookie value.
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletResponse response){
        response.addCookie(cookieService.deleteJwtCookie());
        response.addCookie(cookieService.deleteUserIdCookie());
    }

    /**
     * Verifies user's email with the provided token.
     *
     * @param email The email address to be verified
     * @param token The verification token
     * @return An ApiResponse indicating the success or failure of the operation
     * @throws AlreadyExistsException If the email is already verified
     * @throws TokenExpiredException If the verification token has expired
     * @throws NotFoundException If the user with the specified email is not found
     */
    @GetMapping("/verify-account/{email}")
    public ApiResponse<String> verifyEmail(
            @PathVariable String email,
            @RequestParam String token
    ) throws AlreadyExistsException, TokenExpiredException, NotFoundException {
        return ApiResponse.success(userService.verifyUser(email,token));
    }

    /**
     * Resends the verification email to the user with the specified email address.
     *
     * @param email The email address to which the verification email will be resent
     * @return An ApiResponse indicating the success or failure of the operation
     * @throws NotFoundException If the user with the specified email is not found
     * @throws ForbiddenException If resending the verification email is not allowed
     */
    @PostMapping("/resend-verification-email/{email}")
    public ApiResponse<String> resendVerificationEmail(@PathVariable String email) throws NotFoundException, ForbiddenException {
        return ApiResponse.success(userService.resendVerificationEmail(email));
    }

    /**
     * Sends a one-time password (OTP) to the user's email for resetting the password.
     *
     * @param email The email address for which the OTP will be sent
     * @return An ApiResponse indicating the success or failure of the operation
     * @throws NotFoundException If the user with the specified email is not found
     * @throws ForbiddenException If sending the OTP is not allowed
     */
    @PostMapping("/{email}/forgot-password")
    public ApiResponse<Boolean> sendResetOTP(@PathVariable String email) throws NotFoundException, ForbiddenException {
        return ApiResponse.success(userService.sendResetOTP(email));
    }

    /**
     * Resets the user's password with the provided OTP.
     *
     * @param email The email address of the user whose password will be reset
     * @param req The VerifyOtpRequest containing the OTP and new password
     * @return An ApiResponse indicating the success or failure of the operation
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation
     * @throws NotFoundException If the user with the specified email is not found
     * @throws TokenExpiredException If the OTP has expired
     */
    @PutMapping("/{email}/reset-password")
    public ApiResponse<Boolean> resetPasswordWithOtp(
            @PathVariable String email,
            @RequestBody @Valid VerifyOtpRequest req
    ) throws UnauthorizedAccessException, NotFoundException, TokenExpiredException {
        return ApiResponse.success(userService.verifyOTP(email,req));
    }
}
