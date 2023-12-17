package com.cmj.cmj.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class EncryptPassword {
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_MODE = "ECB/PKCS5Padding";
    private static final String AES_KEY = "clnMeninoJesusEc"; // Substitua pela sua chave

    public static String encrypt(String input) throws Exception {
        Key key = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM + "/" + AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encrypted) throws Exception {
        Key key = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM + "/" + AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
