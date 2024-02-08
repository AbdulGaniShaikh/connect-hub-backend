package com.shaikhabdulgani.ConnectHub.exception;

/**
 * Exception thrown to indicate that a requested resource was not found.
 * This exception is commonly used in REST APIs to handle scenarios where
 * the client is trying to access or manipulate a resource that does not exist,
 * resulting in an HTTP 404 Not Found status.
 */
public class NotFoundException extends Exception{
    /**
     * Constructs a new {@code NotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public NotFoundException(String message) {
        super(message);
    }
}
