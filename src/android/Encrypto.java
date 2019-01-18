package com.encryptotel.encrypto;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import android.util.Base64;

import java.nio.charset.Charset;

public class Encrypto extends CordovaPlugin {

    private static final String DECRYPT = "decrypt";

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

    public static byte[] decodeBase64(String message) {
        return Base64.decode(message, Base64.DEFAULT);
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeBase64(base64PrivateKey));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    private static String decrypt(byte[] secureKey, byte[] iv, byte[] value) throws Exception {
        javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(secureKey, "AES");
        javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv);

        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] decrypted = cipher.doFinal(value);

        String str = new String(decrypted);

        return str;
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] copyOf(byte[] original, int srcPos, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, srcPos, copy, 0,
                Math.min(original.length - srcPos, newLength));
        return copy;
    }

    private static final Charset ASCII = Charset.forName("US-ASCII");
    /**
     * @param base64PrivateKey Private key
     * @param message          Encrypted string
     * @return Decrypted string
     */
    public static String decrypt(String base64PrivateKey, String message) throws Exception {
        String str1 = "-----BEGIN RSA PRIVATE KEY-----";
        int idx1 = base64PrivateKey.indexOf(str1);
        if (idx1 != -1) {
            base64PrivateKey = base64PrivateKey.substring(str1.length());
        }
        String str2 = "-----END RSA PRIVATE KEY-----";
        int idx2 = base64PrivateKey.indexOf(str2);
        if (idx1 != -1) {
            base64PrivateKey = base64PrivateKey.substring(0, idx2);
        }
        base64PrivateKey = base64PrivateKey.replaceAll("\r", "");
        base64PrivateKey = base64PrivateKey.replaceAll("\n", "");
        PrivateKey privateKey = getPrivateKey(base64PrivateKey);

        byte[] messageArr = decodeBase64(message);

        byte[] encSessionKey = copyOf(messageArr, 0, 256);
        byte[] sessionKey = decrypt(encSessionKey, privateKey);

        byte[] encIv = copyOf(messageArr, 256, 256);
        byte[] iv = decrypt(encIv, privateKey);

        byte[] encData = copyOf(messageArr, 512, messageArr.length - 512);
        String data = decrypt(sessionKey, iv, encData).trim();
//        System.out.println(data);

        return data;
    }

}