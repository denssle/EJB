package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Test;

import dbc.PasswordHash;

public class PasswordHashUnitTests {
	@Test
	public void saltTest() {
	  try {
		  byte[] salt = PasswordHash.getSalt();
		  assertNotNull(salt);
	  } catch (NoSuchAlgorithmException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		  }
	  }
	
	@Test
	public void hashTest1() {
		try {
			byte[] salt = PasswordHash.getSalt();
			String pw = "test";
			String hash1 = PasswordHash.getHash(pw, salt);
			String hash2 = PasswordHash.getHash(pw, salt);
			assertEquals(hash1, hash2);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void hashTest2() {
		try {
			String pw1 = "test1";
			String pw2 = "test2";
			String hash1 = PasswordHash.getHash(pw1, PasswordHash.getSalt());
			String hash2 = PasswordHash.getHash(pw2, PasswordHash.getSalt());
			assertNotEquals(hash1, hash2);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void hashTest3() {
		try {
			byte[] salt = PasswordHash.getSalt();
			String hash1 = PasswordHash.getHash("test1", salt);
			String hash2 = PasswordHash.getHash("test2", salt);
			assertNotEquals(hash1, hash2);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}