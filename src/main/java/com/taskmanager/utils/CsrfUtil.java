package com.taskmanager.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder()
                .withoutPadding().encodeToString(randomBytes);
    }
}

