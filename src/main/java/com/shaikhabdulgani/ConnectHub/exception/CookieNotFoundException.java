package com.shaikhabdulgani.ConnectHub.exception;

/**
 * Exception thrown to indicate that a required JWT cookie is not found.
 * This exception is typically used in scenarios where an operation
 * requires the presence of a specific cookie in the HTTP request.
 */
public class CookieNotFoundException extends Exception {
    /**
     * Constructs a new {@code CookieNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public CookieNotFoundException(String message) {
        super(message);
    }
}
