package com.laynet.passwordmanager.security;

import android.util.Base64;

import com.laynet.passwordmanager.Exceptions.CryptoException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private int iterationCount = 1000;
    private int keyLength = 256;
    private int saltLength = keyLength / 8; // same size as key output

    public String encrypt(String plaintext, String password) {

        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[saltLength];
            random.nextBytes(salt);

            SecretKey key = deriveSecretKey(password, salt);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];
            random.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));

            StringBuffer strBuf = new StringBuffer();
            strBuf.append(encodeBase64(iv));
            strBuf.append("]");
            strBuf.append(encodeBase64(salt));
            strBuf.append("]");
            strBuf.append(encodeBase64(ciphertext));
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decrypt(String ciphertext, String password) throws CryptoException {
        try {
            String[] fields = ciphertext.split("]");
            byte[] iv = decodeBase64(fields[0]);
            byte[] salt = decodeBase64(fields[1]);
            byte[] cipherBytes = decodeBase64(fields[2]);

            SecretKey key = deriveSecretKey(password, salt);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            byte[] plaintext = cipher.doFinal(cipherBytes);
            return new String(plaintext , "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            throw new CryptoException(e);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected String encodeBase64(byte[] plaintext) {
        byte[] encodedBytes = Base64.encode(plaintext, Base64.DEFAULT);
        return new String(encodedBytes);
    }

    protected byte[] decodeBase64(String ciphertext) {
        byte[] decodedBytes = Base64.decode(ciphertext, Base64.DEFAULT);
        return decodedBytes;
    }

    private SecretKey deriveSecretKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}
