package com.privy.helper;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionConfig {

	/*
		Requirements:
		1. Input Data
		2. Secret Key
		3. Initialization Vector (IV)
		4. Encrypt and Decrypt
	 */

	private static final String ALGORITHM = "AES/GCM/NoPadding";
	private static final int IV_LENGTH_BYTE = 12;
    private static final int TAG_LENGTH_BIT = 128;
	private static final byte[] STATIC_SALT = {
	    (byte)0x43, (byte)0x76, (byte)0x95, (byte)0x12, 
	    (byte)0x55, (byte)0x88, (byte)0x23, (byte)0x11,
	    (byte)0x34, (byte)0x19, (byte)0x01, (byte)0x77, 
	    (byte)0x81, (byte)0x22, (byte)0x44, (byte)0x90
	};
	
	// This is our Master Key "Safe"
    private static SecretKey masterKey;
	
    // Secret Key
	public static void getKeyFromUserID(int userID) {
		
		try {	
			String idString = String.valueOf(userID);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(idString.toCharArray(), STATIC_SALT, 65536, 256);
            masterKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
	}
	
	// Encrypt
	public static String encrypt(String plainText) {
				
		if (masterKey == null) throw new IllegalStateException("Master Key not initialized!");
		try {
			// Initialization Vector (IV)
            byte[] iv = new byte[IV_LENGTH_BYTE];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, masterKey, gcmSpec); // Use the Master Key
            
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) { e.printStackTrace(); }
        return plainText;
		
	}
	
	// Decrypt
	public static String decrypt(String cipherTextWithIv) {
	    
		if (masterKey == null) return "LOCKED";
        try {
        	// Initialization Vector (IV)
            byte[] decoded = Base64.getDecoder().decode(cipherTextWithIv);
            byte[] iv = new byte[IV_LENGTH_BYTE];
            System.arraycopy(decoded, 0, iv, 0, iv.length);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

            byte[] cipherText = new byte[decoded.length - iv.length];
            System.arraycopy(decoded, iv.length, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, masterKey, gcmSpec); // Use the Master Key
            
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) { e.printStackTrace(); }
        return "DECRYPTION_ERROR";
	}
	
	// When logging out, wipe the key from memory!
    public static void clearMasterKey() {
        masterKey = null;
    }
	
}
