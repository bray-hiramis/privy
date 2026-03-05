package com.privy.model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashPassword {
	
	private String password;
	
	public HashPassword(String pass) {
		byte[] hash = generateHash(pass);
		String hex = bytesToHex(hash);
		this.password = hex;
	}
	
	public String getPassword() {
		return this.password;
	}

	// Password Hashing
    public byte[] generateHash(String password) {
    	byte[] salt = new byte[16];
    	
    	KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
    	try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (InvalidKeySpecException e) {
			System.err.println(e.getMessage());
			return null;
		}
    }
    
    // Convert the generateHash() output to a readable string output
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte bits : bytes) {
            sb.append(String.format("%02x", bits));
        }
        return sb.toString();
    }
	
}
