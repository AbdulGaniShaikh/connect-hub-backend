package com.shaikhabdulgani.ConnectHub.exception;

/**
 * Exception thrown to indicate that the client does not have the necessary
 * authorization or authentication to perform a specific operation.
 * This exception is typically used in REST APIs to handle scenarios where
 * the user's credentials or permissions are insufficient, resulting in
 * an HTTP 401 Unauthorized status.
 */
public class UnauthorizedAccessException extends Exception{

    /**
     * Constructs a new {@code UnauthorizedAccessException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
