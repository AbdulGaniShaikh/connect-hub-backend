package com.shaikhabdulgani.ConnectHub.exception;

/**
 * Exception thrown to indicate that the client does not have permission
 * to access a specific resource or perform a particular operation.
 * This exception is commonly used in REST APIs to handle scenarios where
 * the user lacks the necessary permissions, resulting in an HTTP 403 Forbidden status.
 */
public class ForbiddenException extends Exception {

    /**
     * Constructs a new {@code ForbiddenException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public ForbiddenException(String message) {
        super(message);
    }
}

