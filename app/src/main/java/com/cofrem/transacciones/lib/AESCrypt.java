package com.cofrem.transacciones.lib;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * Created by luisp on 11/10/2017.
 */

public class AESCrypt {

    private Cipher cipher1;
    private SecretKeySpec key1;
    private AlgorithmParameterSpec spec;
    public static final String SEED_16_CHARACTER = "$CREDICOFREM@2018";

    public AESCrypt(){}

    public AESCrypt(String x) throws Exception {
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(SEED_16_CHARACTER.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher1 = Cipher.getInstance("AES/CBC/PKCS7Padding");
        key1 = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }

    // Definición del tipo de algoritmo a utilizar (AES, DES, RSA)
    private final static String alg = "AES";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "AES/CBC/PKCS5Padding";

    public static String encrypt(String cleartext) throws Exception {
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return new String(Base64.encode(encrypted,Base64.DEFAULT), "UTF-8");
    }


    public AlgorithmParameterSpec getIV() {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
//        byte[] iv = { 1,4,2,5,6,3};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }

    public String encrypt2(String plainText) throws Exception {
        cipher1.init(Cipher.ENCRYPT_MODE, key1, spec);
        byte[] encrypted = cipher1.doFinal(plainText.getBytes("UTF-8"));
        String encryptedText = new String(Base64.encode(encrypted,Base64.DEFAULT), "UTF-8");

        return encryptedText;
    }

    public String decrypt(String cryptedText) throws Exception {
        cipher1.init(Cipher.DECRYPT_MODE, key1, spec);
        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] decrypted = cipher1.doFinal(bytes);
        String decryptedText = new String(decrypted, "UTF-8");

        return decryptedText;
    }




}
