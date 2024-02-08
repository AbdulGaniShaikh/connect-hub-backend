package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.FriendRequestData;
import com.shaikhabdulgani.ConnectHub.dto.UnfriendRequest;
import com.shaikhabdulgani.ConnectHub.exception.AlreadyExistsException;
import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private CookieService cookieService;

    @PostMapping
    public void sendFriendRequest(
            @RequestBody @Valid FriendRequestData friendRequest,
            HttpServletRequest request
    ) throws CookieNotFoundException, AlreadyExistsException, UnauthorizedAccessException, NotFoundException {
        friendService.newFriendRequest(friendRequest,cookieService.extractJwtCookie(request));
    }

    @PutMapping("/requests/accept")
    public void acceptFriendRequest(@RequestBody @Valid FriendRequestData requestData, HttpServletRequest request) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        friendService.acceptFriendRequest(requestData,cookieService.extractJwtCookie(request));
    }

    @PutMapping("/requests/reject")
    public void rejectFriendRequest(@RequestBody @Valid FriendRequestData requestData, HttpServletRequest request) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        friendService.rejectFriendRequest(requestData,cookieService.extractJwtCookie(request));
    }

    @PutMapping("/requests")
    public void deleteFriendRequest(@RequestBody @Valid FriendRequestData requestData, HttpServletRequest request) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        friendService.deleteFriendRequest(requestData,cookieService.extractJwtCookie(request));
    }

    @PutMapping
    public void unfriend(@RequestBody @Valid UnfriendRequest unfriendRequest, HttpServletRequest request) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        friendService.unfriend(unfriendRequest,cookieService.extractJwtCookie(request));
    }

}
