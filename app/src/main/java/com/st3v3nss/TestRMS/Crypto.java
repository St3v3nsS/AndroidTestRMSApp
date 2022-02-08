package com.st3v3nss.TestRMS;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Crypto {

    @SuppressLint("GetInstance")
    public static String decrypt(String key, String passwordEnc) {
        byte[] bytesDecoded = Base64.decode(passwordEnc.getBytes(), Base64.DEFAULT);

        String result = "";

        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey genKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, genKey);

            byte[] textDecrypted = cipher.doFinal(bytesDecoded);

            result = new String(textDecrypted);
        }catch (Exception e){
            Log.d("Error", e.toString());
        }
        return result;
    }

    public static String encrypt(String key, String password) {
        String encrypted = "";
        try{
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey genKey = keyFactory.generateSecret(keySpec);
            byte[] cleartext = password.getBytes();

            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding", "BC"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, genKey);
            encrypted = Base64.encodeToString(cipher.doFinal(cleartext),Base64.DEFAULT);

        }catch (Exception e){
            Log.d("Error", e.toString());
        }
        return encrypted;
    }
}
