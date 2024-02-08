package com.shaikhabdulgani.ConnectHub.util.enums;

/**
 * The `AuthenticationMethod` enum represents different methods of authentication,
 * i.e. email and username.
 */
public enum AuthenticationMethod {

    /**
     * Authentication method using email.
     */
    EMAIL,

    /**
     * Authentication method using username.
     */
    USERNAME;

    /**
     * Checks if the authentication method is username.
     *
     * @return true if the authentication method is username, false otherwise.
     */
    public boolean isUsername() {
        return this == AuthenticationMethod.USERNAME;
    }

    /**
     * Checks if the authentication method is email.
     *
     * @return true if the authentication method is email, false otherwise.
     */
    public boolean isEmail() {
        return this == AuthenticationMethod.EMAIL;
    }
}

