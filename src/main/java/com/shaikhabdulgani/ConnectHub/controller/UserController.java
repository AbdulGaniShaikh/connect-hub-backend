package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.ApiResponse;
import com.shaikhabdulgani.ConnectHub.dto.ChangePasswordRequest;
import com.shaikhabdulgani.ConnectHub.dto.UpdateDescription;
import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.*;
import com.shaikhabdulgani.ConnectHub.projection.Feed;
import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.FriendService;
import com.shaikhabdulgani.ConnectHub.service.PostService;
import com.shaikhabdulgani.ConnectHub.service.UserService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final BasicUserService basicUserService;
    private final PostService postService;
    private final CookieService cookieExtractor;
    private final FriendService friendService;

    @GetMapping("/{userId}")
    public ApiResponse<User> getUser(@PathVariable String userId) throws NotFoundException {
        return ApiResponse.success(basicUserService.getById(userId));
    }

    @GetMapping("/isLoggedIn")
    @ResponseStatus(HttpStatus.OK)
    public void isLoggedIn(){
        log.info("user is logged in");
        return;
    }

    @GetMapping("/{userId}/verified")
    public ApiResponse<Boolean> isUserVerified(@PathVariable String userId,HttpServletRequest request) throws CookieNotFoundException, NotFoundException, UnauthorizedAccessException {
        return ApiResponse.success(basicUserService.getUserIsVerified(userId,cookieExtractor.extractJwtCookie(request)));
    }

    @PutMapping("/{userid}/change-password")
    public ApiResponse<Boolean> changePassword(
            @PathVariable String userId,
            @RequestBody @Valid ChangePasswordRequest passwordRequest,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        return ApiResponse.success(
                userService.changePassword(userId,passwordRequest,cookieExtractor.extractJwtCookie(request))
        );
    }

    @PutMapping("/{userId}/updateDescription")
    public ApiResponse<String> updateDescription(
            @PathVariable String userId,
            @RequestBody @Valid UpdateDescription description,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        return ApiResponse.success(
                userService.updateDescription(userId,description,cookieExtractor.extractJwtCookie(request))
        );
    }

    @PutMapping("/{userId}/updateProfile")
    public ApiResponse<String> updateProfile(
            @PathVariable String userId,
            @RequestPart MultipartFile file,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException, IOException {
        return ApiResponse.success(
                userService.updateProfile(userId,file,cookieExtractor.extractJwtCookie(request))
        );
    }

    @PutMapping("/{userId}/updateCover")
    public ApiResponse<String> updateCover(
            @PathVariable String userId,
            @RequestPart MultipartFile file,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException, IOException {
        return ApiResponse.success(
                userService.updateCover(userId,file,cookieExtractor.extractJwtCookie(request))
        );
    }

    @GetMapping("/{userId}/feed")
    public List<Feed> getUserFeed(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return postService.getUserFeed(userId, cookieExtractor.extractJwtCookie(request), pageNumber,pageSize);
    }

    @GetMapping("/{userId}/posts")
    public Page<Post> getUserPosts(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ) throws NotFoundException {
        return postService.getUserPosts(userId,pageNumber,pageSize);
    }

    @GetMapping("/relation/{userId}")
    public ApiResponse<Relation> getRelationWithOtherUser(
            @PathVariable String userId,
            @RequestParam String otherUserId
    ) throws NotFoundException {
        return ApiResponse.success(friendService.getRelation(userId,otherUserId));
    }



    @GetMapping("/{userId}/bookmarks")
    public Page<Bookmark> getUserBookmarks(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return postService.getAllBookmarkedPost(userId,cookieExtractor.extractJwtCookie(request),pageNumber,pageSize);
    }

    @GetMapping("/{userId}/friends")
    public Page<Friend> getUserFriends(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return friendService.getAllFriends(userId,cookieExtractor.extractJwtCookie(request),pageNumber,pageSize);
    }

    @GetMapping("/{userId}/friend-requests")
    public Page<FriendRequest> getUserFriendRequests(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            HttpServletRequest request
    ) throws NotFoundException, CookieNotFoundException, UnauthorizedAccessException {
        return friendService.getAllFriendsRequest(userId,cookieExtractor.extractJwtCookie(request),pageNumber,pageSize);
    }

}
