package com.laynet.passwordmanager.security;

import com.laynet.passwordmanager.Exceptions.CryptoException;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static org.junit.Assert.assertEquals;

public class CryptoTest {
    private class MyCrypto extends Crypto {
        protected String encodeBase64(byte[] plaintext) {
            byte[] encodedBytes = java.util.Base64.getEncoder().encode(plaintext);
            return new String(encodedBytes);
        }

        protected byte[] decodeBase64(String ciphertext) {
            byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
            return decodedBytes;
        }
    }

    @Test
    public void encrypt_string_and_decrypt_with_the_same_password_should_return_the_same_string() throws CryptoException {
        String password = "password";
        String plaintext = "test string";

        Crypto crypto = new MyCrypto();
        String ciphertext = crypto.encrypt(plaintext, password);
        String actualPlaintext = crypto.decrypt(ciphertext, password);

        assertEquals(plaintext, actualPlaintext);
    }

    @Test(expected = CryptoException.class)
    public void encrypt_string_and_decrypt_with_a_different_password_should_throw() throws CryptoException {
        String password = "password";
        String anotherPassword = "anotherPassword";
        String plaintext = "test string";

        Crypto crypto = new MyCrypto();
        String ciphertext = crypto.encrypt(plaintext, password);
        crypto.decrypt(ciphertext, anotherPassword);
    }
}
