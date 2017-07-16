package com.terrier.finances.gestion.business.auth;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Password Encoder
 * @author vzwingma
 *
 */
public class PasswordEncoder {

	

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoder.class);

	
	private PasswordEncoder() {  }

	/**
	 * @param password  mot de passe
	 * @return mot de passe hashé
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String generateStrongPasswordHash(String password)
	{
		try{
			int iterations = 1000;
			char[] chars = password.toCharArray();
			byte[] salt = getSalt();

			PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash;

			hash = skf.generateSecret(spec).getEncoded();

			return iterations + ":" + toHex(salt) + ":" + toHex(hash);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * @return salt
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}


	/**
	 * Validation du mot de passe
	 * @param originalPassword
	 * @param storedPassword
	 * @return résultat de la valisation
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static boolean validatePassword(String originalPassword, String storedPassword)
	{
		try{
			String[] parts = storedPassword.split(":");
			int iterations = Integer.parseInt(parts[0]);
			byte[] salt = fromHex(parts[1]);
			byte[] hash = fromHex(parts[2]);

			PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] testHash = skf.generateSecret(spec).getEncoded();

			int diff = hash.length ^ testHash.length;
			for(int i = 0; i < hash.length && i < testHash.length; i++)
			{
				diff |= hash[i] ^ testHash[i];
			}
			return diff == 0;
		}
		catch(NoSuchAlgorithmException | InvalidKeySpecException e){
			LOGGER.error("Erreur lors du calcul du chiffrement", e);
			return false;
		}
		
	}


	/**
	 * @param hex
	 * @return bytes[]
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
	{
		byte[] bytes = new byte[hex.length() / 2];
		for(int i = 0; i<bytes.length ;i++)
		{
			bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	/**
	 * @param array
	 * @return String from hex
	 * @throws NoSuchAlgorithmException
	 */
	private static String toHex(byte[] array) throws NoSuchAlgorithmException
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if(paddingLength > 0)
		{
			return String.format("%0"  +paddingLength + "d", 0) + hex;
		}else{
			return hex;
		}
	}

}