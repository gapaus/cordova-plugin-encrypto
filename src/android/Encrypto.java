package com.ideas2it.aes256;

import android.util.Base64;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import shaded.org.apache.commons.codec.binary.Hex;

public class Encrypto extends CordovaPlugin {

    private static final String DECRYPT = "decrypt";

    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static final int PBKDF2_ITERATION_COUNT = 1001;
    private static final int PBKDF2_KEY_LENGTH = 256;
    private static final int SECURE_IV_LENGTH = 64;
    private static final int SECURE_KEY_LENGTH = 128;
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String PBKDF2_SALT = "hY0wTq6xwc6ni01G";
    private static final Random RANDOM = new SecureRandom();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (DECRYPT.equalsIgnoreCase(action)) {
                String privateKey = args.getString(0);
                String message = args.getString(1);
                callbackContext.success(decrypt(privateKey, message));
                return true;
            } else {
                callbackContext.error("Invalid method call");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error occurred while performing " + action + " : " + e.getMessage());
            callbackContext.error("Error occurred while performing " + action);
        }
        return false;
    }

    /**
     * @param secureKey A 32 bytes string, which will used as input key for Encrypto decryption
     * @param value     A 16 bytes string, which will used as initial vector for Encrypto decryption
     * @param iv        An Encrypto encrypted data which will be decrypted
     * @return AES Decrypted string
     * @throws Exception
     */
    private String decrypt(String privateKey, String message) throws Exception {
//        byte[] pbkdf2SecuredKey = generatePBKDF2(secureKey.toCharArray(), PBKDF2_SALT.getBytes("UTF-8"),
//                PBKDF2_ITERATION_COUNT, PBKDF2_KEY_LENGTH);
//
//        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
//        SecretKeySpec secretKeySpec = new SecretKeySpec(pbkdf2SecuredKey, "AES");
//
//        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
//
//        byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));
//
//        return new String(original);
        return "test";
    }

}