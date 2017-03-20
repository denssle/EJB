package dbc;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHash {
	public static byte[] getSalt() throws NoSuchAlgorithmException {
		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
		return salt;
	}

	public static String getHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = f.generateSecret(spec).getEncoded();
		Base64.Encoder enc = Base64.getEncoder();
		return enc.encodeToString(hash);
	}

	public static String getSaltedHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = getSalt();
		String hash = getHash(password, salt);
		return combineHashAndSalt(hash, salt);
	}
	
	private static String combineHashAndSalt(String hash, byte[] salt) {
		Base64.Encoder enc = Base64.getEncoder();
		return enc.encodeToString(salt) + "§" + hash;
	}

	
	private static String[] separateHashAndSalt(String hashAndSalt) {
		return hashAndSalt.split("\\§");
	}
	
	public static boolean passwordCheck(String password, String stored) throws Exception{
	    String[] saltAndPass = separateHashAndSalt(stored);
	    Base64.Decoder dec = Base64.getDecoder();
	    byte[] salt = dec.decode(saltAndPass[0]);
	    String hash = getHash(password, salt);
	    String hashed =  combineHashAndSalt(hash, salt);
	    System.out.println("check password: " + password + " stored: " + stored + " new hashed: " + hashed + hashed.equals(stored));
	    if(hashed.equals(stored)) {
	    	return true;
	    }
	    return false;
	}
}