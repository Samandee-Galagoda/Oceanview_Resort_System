package com.oceanview.resort.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtil {
    private PasswordUtil() {
    }

    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : encoded) {
                builder.append(String.format("%02x", b & 0xff));
            }
            return builder.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash password", ex);
        }
    }
}
