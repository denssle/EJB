package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import bean.User;

public class UserUnitTests {
	
	@Test
	public void userTest1() {
		int id = 2;
		String name = "NAME";
		String pw = "PW";
		User user = new User(id, name, pw, null, "");
		assertEquals(user.getId(),id);
		assertEquals(user.getName(),name);
		assertEquals(user.getPassword(),pw);
		assertNotEquals(user.getId(),id*id);
		assertNotEquals(user.getName(),name+pw);
		assertNotEquals(user.getPassword(),pw+id);
	}
	
	@Test
	public void userTest2() {
		User user = new User(1, "x", "y", null, "");
		assertEquals(user.getAppIds().size(), 0);
	}
	
	@Test
	public void userTest3() {
		User user = new User(1, "x", "y", null, "5");
		assertEquals(user.getAppIds().size(), 1);
	}
	
	@Test
	public void userTest3_1() {
		User user = new User(1, "x", "y", null, "55555");
		assertEquals(user.getAppIds().size(), 1);
	}
	
	@Test
	public void userTest4() {
		User user = new User(1, "x", "y", null, "5,5");
		assertEquals(user.getAppIds().size(), 1);
	}
	
	@Test
	public void userTest5() {
		User user = new User(1, "x", "y", null, "1,5");
		assertEquals(user.getAppIds().size(), 2);
	}
	
	@Test
	public void userTest6() {
		User user = new User(1, "x", "y", null, "1,5,,,,,");
		assertEquals(user.getAppIds().size(), 2);
	}
	
	@Test
	public void userTest7() {
		User user = new User(1, "x", "y", null, "ioadwi,2");
		assertEquals(user.getAppIds().size(), 1);
	}
	
	@Test
	public void userTest8() {
		User user = new User(1, "x", "y", null, "1,currupt,2");
		assertEquals(user.getAppIds().size(), 2);
	}
	
	@Test
	public void userTest9() {
		User user = new User(1, "x", "y", null, "1.2");
		assertEquals(user.getAppIds().size(), 0);
	}
}