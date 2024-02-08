package com.shaikhabdulgani.ConnectHub.exception;

/**
 * Exception thrown to indicate that an entity already exists in the system.
 * This exception is commonly used in REST advice to handle cases where
 * the creation or addition of a resource conflicts with an existing one,
 * resulting in an HTTP 409 Conflict status.
 */
public class AlreadyExistsException extends Exception{
    /**
     * Constructs a new {@code AlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public AlreadyExistsException(String message) {
        super(message);
    }
}
