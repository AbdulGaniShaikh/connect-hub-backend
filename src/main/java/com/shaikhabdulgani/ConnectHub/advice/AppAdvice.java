package com.shaikhabdulgani.ConnectHub.advice;

import com.shaikhabdulgani.ConnectHub.exception.*;
import com.shaikhabdulgani.ConnectHub.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global controller advice to handle exceptions thrown from controllers.
 */
@RestControllerAdvice
public class AppAdvice {

    /**
     * Handles MethodArgumentNotValidException thrown when method argument validation fails.
     *
     * @param ex The MethodArgumentNotValidException instance
     * @return A map containing field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleInvalidArgument(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach( error ->{
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return errorMap;
    }

    /**
     * Handles BadRequestException thrown when a bad request is encountered.
     *
     * @param ex The BadRequestException instance
     * @return An ApiError object representing the bad request
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(BadRequestException ex){
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles NotFoundException thrown when a requested resource is not found.
     *
     * @param ex The NotFoundException instance
     * @return An ApiError object representing the not found status
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException ex){
        return new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles UnauthorizedAccessException thrown when access is unauthorized.
     *
     * @param ex The UnauthorizedAccessException instance
     * @return An ApiError object representing the unauthorized access
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleUnauthorizedAccessException(UnauthorizedAccessException ex){
        return new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Handles TokenExpiredException thrown when a token has expired.
     *
     * @param ex The TokenExpiredException instance
     * @return An ApiError object representing the token expiration
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleTokenExpiredException(TokenExpiredException ex){
        return new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Handles AlreadyExistsException thrown when a resource already exists.
     *
     * @param ex The AlreadyExistsException instance
     * @return An ApiError object representing the conflict due to existing resource
     */
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistsException(AlreadyExistsException ex){
        return new ApiError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles CookieNotFoundException thrown when a required cookie is not found.
     *
     * @param ex The CookieNotFoundException instance
     * @return An ApiError object representing the missing cookie
     */
    @ExceptionHandler(CookieNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCookieNotFoundException(CookieNotFoundException ex){
        return new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Handles ForbiddenException thrown when access is forbidden.
     *
     * @param ex The ForbiddenException instance
     * @return An ApiError object representing the forbidden access
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException ex){
        return new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
}

