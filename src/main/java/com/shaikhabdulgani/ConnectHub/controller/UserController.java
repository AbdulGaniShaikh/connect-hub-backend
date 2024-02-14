package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.ApiResponse;
import com.shaikhabdulgani.ConnectHub.dto.ChangePasswordRequest;
import com.shaikhabdulgani.ConnectHub.dto.UpdateDescription;
import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.*;
import com.shaikhabdulgani.ConnectHub.projection.PostProjection;
import com.shaikhabdulgani.ConnectHub.projection.FriendProjection;
import com.shaikhabdulgani.ConnectHub.projection.FriendRequestProjection;
import com.shaikhabdulgani.ConnectHub.projection.SavedProjection;
import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.FriendService;
import com.shaikhabdulgani.ConnectHub.service.PostService;
import com.shaikhabdulgani.ConnectHub.service.UserService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import com.shaikhabdulgani.ConnectHub.util.CustomPage;
import com.shaikhabdulgani.ConnectHub.util.enums.Relation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for handling user-related operations.
 * This controller provides endpoints for user profile management, post retrieval, friend management, and more.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final BasicUserService basicUserService;
    private final PostService postService;
    private final CookieService cookieService;
    private final FriendService friendService;

    /**
     * Retrieves user information by user ID.
     *
     * @param userId The ID of the user to retrieve
     * @return An ApiResponse containing the user information and success flag
     * @throws NotFoundException If the requested user is not found
     */
    @GetMapping("/{userId}")
    public ApiResponse<User> getUser(@PathVariable String userId) throws NotFoundException {
        return ApiResponse.success(basicUserService.getById(userId));
    }

    /**
     * Checks if a user is logged in.
     * This endpoint is used to verify if a user session is active.
     */
    @GetMapping("/isLoggedIn")
    @ResponseStatus(HttpStatus.OK)
    public void isLoggedIn(){
        log.info("user is logged in");
    }

    /**
     * Checks if a user is verified.
     *
     * @param userId The ID of the user to check
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return An ApiResponse indicating whether the user is verified with a success flag
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws NotFoundException If the requested user is not found
     * @throws UnauthorizedAccessException If access is unauthorized
     */
    @GetMapping("/{userId}/verified")
    public ApiResponse<Boolean> isUserVerified(@PathVariable String userId,HttpServletRequest request) throws CookieNotFoundException, NotFoundException, UnauthorizedAccessException {
        return ApiResponse.success(basicUserService.getUserIsVerified(userId, cookieService.extractJwtCookie(request)));
    }

    /**
     * Changes the password for a user.
     *
     * @param userId The ID of the user whose password will be changed
     * @param passwordRequest The ChangePasswordRequest object containing the new password and old password
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return An ApiResponse indicating the success or failure of the password change operation
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized to change password or
     * if old password and new password doesn't match
     * @throws NotFoundException If the requested user is not found
     */
    @PutMapping("/{userid}/change-password")
    public ApiResponse<Boolean> changePassword(
            @PathVariable String userId,
            @RequestBody @Valid ChangePasswordRequest passwordRequest,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        return ApiResponse.success(
                userService.changePassword(userId,passwordRequest, cookieService.extractJwtCookie(request))
        );
    }

    /**
     * Updates the description of a user.
     *
     * @param userId The ID of the user whose profile description will be updated
     * @param description The UpdateDescription object containing the new profile description
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return An ApiResponse indicating the success or failure of the profile description update operation
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized to update the description
     * @throws NotFoundException If the requested user is not found
     */
    @PutMapping("/{userId}/updateDescription")
    public ApiResponse<String> updateDescription(
            @PathVariable String userId,
            @RequestBody @Valid UpdateDescription description,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        return ApiResponse.success(
                userService.updateDescription(userId,description, cookieService.extractJwtCookie(request))
        );
    }

    /**
     * Updates the profile image of a user.
     *
     * @param userId The ID of the user whose profile image will be updated
     * @param file The Image object containing the new image
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return An ApiResponse indicating the success or failure of the profile image update operation
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized to update the profile image
     * @throws NotFoundException If the requested user is not found
     */
    @PutMapping("/{userId}/updateProfile")
    public ApiResponse<String> updateProfile(
            @PathVariable String userId,
            @RequestPart MultipartFile file,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException, IOException {
        return ApiResponse.success(
                userService.updateProfile(userId,file, cookieService.extractJwtCookie(request))
        );
    }

    /**
     * Updates the profile image of a user.
     *
     * @param userId The ID of the user whose cover image will be updated
     * @param file The Image object containing the new cover image
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return An ApiResponse indicating the success or failure of the cover image update operation
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized to update the cover image
     * @throws NotFoundException If the requested user is not found
     */
    @PutMapping("/{userId}/updateCover")
    public ApiResponse<String> updateCover(
            @PathVariable String userId,
            @RequestPart MultipartFile file,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException, IOException {
        return ApiResponse.success(
                userService.updateCover(userId,file, cookieService.extractJwtCookie(request))
        );
    }

    /**
     * Retrieves the feed of posts for a specific user.
     *
     * @param userId The ID of the user whose feed will be retrieved
     * @param pageNumber The page number for pagination (default is 0)
     * @param pageSize The number of posts per page (default is 10)
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return A list of PostProjection objects containing post information such as post image, post text, etc
     * @throws NotFoundException If the user is not found
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized
     */
    @GetMapping("/{userId}/feed")
    public List<PostProjection> getUserFeed(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return postService.getUserFeed(userId, cookieService.extractJwtCookie(request), pageNumber,pageSize);
    }

    /**
     * Retrieves the posts of a specific user.
     *
     * @param userId The ID of the user whose posts will be retrieved
     * @param pageNumber The page number for pagination (default is 0)
     * @param pageSize The number of posts per page (default is 10)
     * @return A list of PostProjection objects containing post information such as post image, post text, etc
     * @throws NotFoundException If the user is not found
     */
    @GetMapping("/{userId}/posts")
    public Page<Post> getUserPosts(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ) throws NotFoundException {
        return postService.getUserPosts(userId,pageNumber,pageSize);
    }

    /**
     * Retrieves the relation between two users.
     *
     * @param userId The ID of the first user
     * @param otherUserId The ID of the other user
     * @return An ApiResponse containing the relation between the two users
     * @throws NotFoundException If any of the users are not found
     */
    @GetMapping("/relation/{userId}")
    public ApiResponse<Relation> getRelationWithOtherUser(
            @PathVariable String userId,
            @RequestParam String otherUserId
    ) throws NotFoundException {
        return ApiResponse.success(friendService.getRelation(userId,otherUserId));
    }


    /**
     * Retrieves the bookmarks of a specific user.
     *
     * @param userId The ID of the user whose bookmarks will be retrieved
     * @param pageNumber The page number for pagination (default is 0)
     * @param pageSize The number of bookmarks per page (default is 10)
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return A CustomPage object containing the user's bookmarks
     * @throws NotFoundException If the user is not found
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized
     */
    @GetMapping("/{userId}/bookmarks")
    public CustomPage<SavedProjection> getUserBookmarks(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return postService.getAllBookmarkedPost(userId, cookieService.extractJwtCookie(request),pageNumber,pageSize);
    }

    /**
     * Retrieves the friends of a specific user.
     *
     * @param userId The ID of the user whose friends will be retrieved
     * @param pageNumber The page number for pagination (default is 0)
     * @param pageSize The number of friends per page (default is 10)
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return A CustomPage object containing the user's friends
     * @throws NotFoundException If the user is not found
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized
     */
    @GetMapping("/{userId}/friends")
    public CustomPage<FriendProjection> getUserFriends(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return friendService.getAllFriends(userId, cookieService.extractJwtCookie(request),pageNumber,pageSize);
    }

    /**
     * Retrieves the friend requests received by a specific user.
     *
     * @param userId The ID of the user whose friend requests will be retrieved
     * @param pageNumber The page number for pagination (default is 0)
     * @param pageSize The number of friend requests per page (default is 10)
     * @param request The HttpServletRequest object to fetch JWT token from cookies
     * @return A CustomPage object containing the user's friend requests
     * @throws NotFoundException If the user is not found
     * @throws CookieNotFoundException If the JWT cookie is not found
     * @throws UnauthorizedAccessException If access is unauthorized
     */
    @GetMapping("/{userId}/friend-requests")
    public CustomPage<FriendRequestProjection> getUserFriendRequests(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return friendService.getAllFriendsRequest(userId, cookieService.extractJwtCookie(request),pageNumber,pageSize);
    }

}
