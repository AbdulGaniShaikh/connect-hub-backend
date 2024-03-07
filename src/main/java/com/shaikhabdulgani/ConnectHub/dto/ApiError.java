package com.shaikhabdulgani.ConnectHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * The `ApiError` class represents an error response structure for API operations.
 * It includes information such as HTTP status code, error message, error type, and the timestamp of the error occurrence.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    /**
     * The HTTP status code representing the error.
     */
    private int code;

    /**
     * A brief error message providing additional details about the error.
     */
    private String message;

    /**
     * The type of error, typically derived from the HTTP status code's reason phrase.
     */
    private String type;

    /**
     * The timestamp indicating when the error occurred.
     */
    private Date date;

    /**
     * A boolean value indicating whether user should log out or not.
     */
    private boolean logout;

    /**
     * Constructs a new {@code ApiError} instance with the specified HTTP status code and error message.
     *
     * @param code    The HTTP status code representing the error.
     * @param message A brief error message providing additional details about the error.
     */
    public ApiError(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
        this.type = code.getReasonPhrase();
        this.date = new Date();
    }

    /**
     * Constructs a new {@code ApiError} instance with the specified HTTP status code,logout flag and error message.
     *
     * @param code    The HTTP status code representing the error.
     * @param message A brief error message providing additional details about the error.
     */
    public ApiError(HttpStatus code, String message,boolean logout) {
        this.code = code.value();
        this.message = message;
        this.type = code.getReasonPhrase();
        this.date = new Date();
        this.logout = logout;
    }
}

