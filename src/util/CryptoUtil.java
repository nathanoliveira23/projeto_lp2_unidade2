package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {

    private static final SecureRandom RAND = new SecureRandom();
    private static final int SALT_LEN = 16;
    private static final int IV_LEN = 12;
    private static final int KEY_LEN = 256;
    private static final int ITERATIONS = 65536;

    public static byte[] newSalt() {
        byte[] s = new byte[SALT_LEN];   /**< Buffer para armazenar o salt. */
        RAND.nextBytes(s);               /**< Preenche o salt com bytes aleatórios. */

        return s;                        /**< Retorna o salt gerado. */
    }

    public static String SHA256(String password, byte[] salt) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            digest.update(salt);

            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));

            StringBuilder hexHash = new StringBuilder();

            for (byte b : hashBytes)
                hexHash.append(String.format("%02x", b));

            return hexHash.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public static Boolean SHA256Match(String plainPasswd, String hashedPasswd, byte[] salt) throws Exception {
        String candidateHash = SHA256(plainPasswd, salt);

        byte[] candidateHashBytes = Base64.getDecoder().decode(candidateHash);
        byte[] hashedPasswdBytes = Base64.getDecoder().decode(hashedPasswd);

        return MessageDigest.isEqual(candidateHashBytes, hashedPasswdBytes);
    }

    public static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LEN);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        byte[] iv = new byte[IV_LEN];
        RAND.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));

        byte[] combined = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String b64combined, SecretKey key) throws Exception {
        byte[] combined = Base64.getDecoder().decode(b64combined);
        byte[] iv = Arrays.copyOfRange(combined, 0, IV_LEN);
        byte[] cipherText = Arrays.copyOfRange(combined, IV_LEN, combined.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] plain = cipher.doFinal(cipherText);

        return new String(plain, "UTF-8");
    }
}
