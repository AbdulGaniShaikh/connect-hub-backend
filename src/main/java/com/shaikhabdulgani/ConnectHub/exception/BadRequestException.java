package com.shaikhabdulgani.ConnectHub.exception;

/**
 * Exception thrown to indicate that the client has made a malformed or invalid request.
 * This exception is typically used in REST APIs to handle scenarios where the request
 * does not meet the expected format or contains invalid data, resulting in an HTTP 400 Bad Request status.
 */
public class BadRequestException extends Exception{
    /**
     * Constructs a new {@code BadRequestException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public BadRequestException(String message) {
        super(message);
    }
}
