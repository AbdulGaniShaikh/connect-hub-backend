package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.ApiResponse;
import com.shaikhabdulgani.ConnectHub.dto.NewComment;
import com.shaikhabdulgani.ConnectHub.dto.NewPostInfo;
import com.shaikhabdulgani.ConnectHub.exception.BadRequestException;
import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.Comment;
import com.shaikhabdulgani.ConnectHub.model.Post;
import com.shaikhabdulgani.ConnectHub.projection.CommentDto;
import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The `PostController` class handles API endpoints related to posts, including creating new posts,
 * retrieving posts, liking/unliking posts, bookmarking/removing bookmarks, and checking if a post is liked or bookmarked.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    /**
     * cookieService is used to extract cookies
     * */

    @Autowired
    private CookieService cookieService;

    @Autowired
    private PostService postService;

    /**
     * Creates a new post with an optional image and post information.
     *
     * @param image    The image file associated with the post (optional).
     * @param userId The post information containing details like userId.
     * @param text The post information containing details like content.
     * @param request  The HttpServletRequest to extract JWT cookie.
     * @return An ApiResponse containing the newly created post.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     * @throws BadRequestException          If the request is malformed or contains invalid data.
     * @throws IOException                  If an I/O error occurs while processing the image.
     */
    @PostMapping
    public ApiResponse<Post> newPost(
            @RequestPart String userId,
            @RequestPart(required = false) String text,
            @RequestPart(required = false) MultipartFile image,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException, BadRequestException, IOException {
        return ApiResponse.success(postService.uploadNewPost(image,userId,text,cookieService.extractJwtCookie(request)));
    }

    /**
     * Retrieves a post by its unique identifier.
     *
     * @param postId The unique identifier of the post.
     * @return An ApiResponse containing the retrieved post.
     * @throws NotFoundException If the requested post is not found.
     */
    @GetMapping("/{postId}")
    public ApiResponse<Post> getPost(@PathVariable String postId) throws NotFoundException {
        return ApiResponse.success(postService.getPost(postId));
    }

    /**
     * Likes a specific post for the given user.
     *
     * @param postId  The unique identifier of the post to be liked.
     * @param userId  The unique identifier of the user liking the post.
     * @param request The HttpServletRequest to extract JWT cookie.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     */
    @PostMapping("/{postId}/likes/{userId}")
    public void likePost(
            @PathVariable String postId,
            @PathVariable String userId,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        postService.postLiked(postId,userId, cookieService.extractJwtCookie(request));
    }

    /**
     * Unlikes a specific post for the given user.
     *
     * @param postId  The unique identifier of the post to be unliked.
     * @param userId  The unique identifier of the user unliking the post.
     * @param request The HttpServletRequest to extract JWT cookie.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     */
    @DeleteMapping("/{postId}/unlikes/{userId}")
    public void unLikePost(
            @PathVariable String postId,
            @PathVariable String userId,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        postService.postUnliked(postId,userId, cookieService.extractJwtCookie(request));
    }

    /**
     * Checks if a specific post is liked by the given user.
     *
     * @param postId  The unique identifier of the post.
     * @param userId  The unique identifier of the user.
     * @param request The HttpServletRequest to extract JWT cookie.
     * @return An ApiResponse containing a boolean indicating if the post is liked by the user.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     */
    @GetMapping("/{postId}/isLiked/{userId}")
    public ApiResponse<Boolean> isLiked(
            @PathVariable String postId,
            @PathVariable String userId,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        return ApiResponse.success(postService.isLiked(postId,userId, cookieService.extractJwtCookie(request)));
    }

    /**
     * Bookmarks a specific post for the given user.
     *
     * @param postId  The unique identifier of the post to be bookmarked.
     * @param userId  The unique identifier of the user bookmarking the post.
     * @param request The HttpServletRequest to extract JWT cookie.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     */
    @PostMapping("/{postId}/bookmark/{userId}")
    public void bookmarkPost(
            @PathVariable String postId,
            @PathVariable String userId,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        postService.bookmark(postId,userId, cookieService.extractJwtCookie(request));
    }

    /**
     * Removes a bookmark from a specific post for the given user.
     *
     * @param postId  The unique identifier of the post to remove the bookmark from.
     * @param userId  The unique identifier of the user removing the bookmark.
     * @param request The HttpServletRequest to extract JWT cookie.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     */
    @DeleteMapping("/{postId}/removeBookmark/{userId}")
    public void removeBookmark(
            @PathVariable String postId,
            @PathVariable String userId,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        postService.removeBookmark(postId,userId, cookieService.extractJwtCookie(request));
    }

    /**
     * Checks if a specific post is bookmarked by the given user.
     *
     * @param postId  The unique identifier of the post.
     * @param userId  The unique identifier of the user.
     * @param request The HttpServletRequest to extract JWT cookie.
     * @return An ApiResponse containing a boolean indicating if the post is bookmarked by the user.
     * @throws CookieNotFoundException      If JWT cookie is not found in the request.
     * @throws UnauthorizedAccessException If the user is not authorized to perform the operation.
     * @throws NotFoundException            If the requested resource is not found.
     */
    @GetMapping("/{postId}/isBookmarked/{userId}")
    public ApiResponse<Boolean> isBookmarked(
            @PathVariable String postId,
            @PathVariable String userId,
            HttpServletRequest request
    ) throws CookieNotFoundException, UnauthorizedAccessException, NotFoundException {
        return ApiResponse.success(postService.isBookmarked(postId,userId, cookieService.extractJwtCookie(request)));
    }

    @PostMapping("/{postId}/comments")
    public Comment postComment(
            @PathVariable String postId,
            @RequestBody @Valid NewComment newComment,
            HttpServletRequest request
    ) throws CookieNotFoundException {
        return postService.postComment(postId,newComment, cookieService.extractJwtCookie(request));
    }


    @GetMapping("/{postId}/comments")
    public List<CommentDto> getPostComments(
            @PathVariable String postId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ){
        return postService.getComment(postId,pageNumber,pageSize);
    }

}
