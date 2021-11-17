/*
 * TokenGenerator.java
 * Author : 박찬형
 * Created Date : 2021-09-03
 */
package com.codrest.teriser.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecureTokenGenerator {
    private static final int SIZE = 32;

    public static String generateToken(){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] randomBytes = new byte[SIZE];
            new SecureRandom().nextBytes(randomBytes);
            byte[] part = new byte[SIZE / 2];

            System.arraycopy(randomBytes, 0, part, 0, part.length);
            messageDigest.update(part);
            String t1 = Base64.getUrlEncoder().encodeToString(messageDigest.digest());

            System.arraycopy(randomBytes, SIZE / 2, part, 0, part.length);
            messageDigest.update(part);
            String t2 = Base64.getUrlEncoder().encodeToString(messageDigest.digest());

            part = new byte[SIZE / 2];
            new SecureRandom().nextBytes(part);
            byte[] part2 = (t1 + t2).getBytes(StandardCharsets.UTF_8);
            randomBytes = new byte[part.length + part2.length];
            System.arraycopy(part2, 0, randomBytes, 0, part2.length);
            System.arraycopy(part, 0, randomBytes, part2.length, part.length);
            messageDigest.update(randomBytes);
            String t3 = Base64.getUrlEncoder().encodeToString(messageDigest.digest());

            return t1 + t2 + t3;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
