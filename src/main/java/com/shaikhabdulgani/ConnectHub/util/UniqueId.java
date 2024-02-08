package com.shaikhabdulgani.ConnectHub.util;

import java.util.Random;

public class UniqueId {

    public static String generateToken(){
        String validCharacters = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
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

}
