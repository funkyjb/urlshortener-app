package com.example.urlshortener.service.utils;

public class ShortenerUtils {

    public static final int PASSWORD_LENGTH = 8;
    public static final int URL_ID_LENGTH = 6;

    public static String generateAlphanumericString(int length) {
        String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyz";
        String alphabetsInUpperCase = alphabetsInLowerCase.toUpperCase();
        String numbers = "0123456789";
        String allCharacters = alphabetsInLowerCase + alphabetsInUpperCase + numbers;
        StringBuffer randomString = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * allCharacters.length());
            randomString.append(allCharacters.charAt(randomIndex));
        }
        return randomString.toString();
    }
}
