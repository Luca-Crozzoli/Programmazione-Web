package com.units.it.utils;


import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class PasswordSaltHashCheck {
    //https://www.baeldung.com/java-password-hashing
    // https://dev.to/awwsmm/how-to-encrypt-a-password-in-java-42dh
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 512);
        Arrays.fill(password.toCharArray(), Character.MIN_VALUE);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hPassword = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return null;
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean isPasswordRight(String pswInput, String pswSaved, String salt) {
        String pswInputHash = hashPassword(pswInput, salt);
        return pswInputHash.equals(pswSaved);
    }
}
