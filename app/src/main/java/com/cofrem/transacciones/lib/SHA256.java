package com.cofrem.transacciones.lib;

import java.security.MessageDigest;

/**
 * Created by luisp on 12/10/2017.
 */

public class SHA256 {

    public static String crypt(String s) {
        MessageDigest digest;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(s.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
