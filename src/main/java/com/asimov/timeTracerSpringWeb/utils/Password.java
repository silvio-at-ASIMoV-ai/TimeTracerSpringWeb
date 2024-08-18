package com.asimov.timeTracerSpringWeb.utils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

public class Password {
    private static final int keyLength = 64;
    private static final int iterations = 100000;

    private static String toHexEncodedString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < bArr.length; i++) {
            String c = String.format("%02X", bArr[i]);
            sb.append(c);
        }
        return sb.toString();
    }

    public static Optional<String> createHashedAndSaltedPassword(String clear) {
        SecureRandom random = new SecureRandom();
        byte[] saltArr = new byte[32];
        random.nextBytes(saltArr);
        String salt = toHexEncodedString(saltArr);
        Optional<String> hashedPwd = hashPassword(clear, salt);
        if(hashedPwd.isPresent()) {
            return Optional.of(hashedPwd.get() + salt);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> hashPassword(String password, String salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(),
                    iterations, keyLength * 4);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return Optional.of(toHexEncodedString(res));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e ) {
            return Optional.empty();
        }
    }

    public static boolean checkPassword(String clear, String encrypted) {
        String salt = encrypted.substring(keyLength, keyLength * 2);
        String pwd = encrypted.substring(0, keyLength);
        Optional<String> optHashedPwd = hashPassword(clear, salt);
        if(optHashedPwd.isPresent()) {
            return optHashedPwd.get().equals(pwd);
        } else {
            return false;
        }
    }

}
