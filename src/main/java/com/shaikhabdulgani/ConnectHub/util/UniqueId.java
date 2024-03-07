package com.shaikhabdulgani.ConnectHub.util;

import java.util.Random;

/**
 * Utility class for generating unique identifiers.
 * This class provides a method to generate random tokens with a specific format.
 */
public class UniqueId {

    private static final String validCharacters = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generates a random token with a specific format.
     *
     * @return A randomly generated token
     */
    public static String generateToken() {
        int idLength = 36;
        StringBuilder randomId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < idLength; i++) {
            randomId.append(validCharacters.charAt(random.nextInt(validCharacters.length())));
            if ((i + 1) % 8 == 0 && i < idLength - 1) {
                randomId.append("-");
            }
        }
        return randomId.toString();
    }

    /**
     * Generates a refresh token with a specific format.
     *
     * @return A randomly generated token
     */
    public static String generateRefreshToken() {
        int idLength = 20;
        StringBuilder randomId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < idLength; i++) {
            randomId.append(validCharacters.charAt(random.nextInt(validCharacters.length())));
        }
        return randomId.toString();
    }
}
