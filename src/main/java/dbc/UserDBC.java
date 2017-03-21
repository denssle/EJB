package dbc;

import static org.jooq.h2.generated.Tables.USERS;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;

import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.*;

import bean.User;

public class UserDBC {
	private static Connection connection;
	private static DSLContext create;
	private static ArrayList<User> users; 
	
    public static void connect() {    	
    	try {
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:~/test";
			String user = "sa";
			String pw = "";
			connection = DriverManager.getConnection(url, user, pw);
			create = DSL.using(connection, SQLDialect.H2);
			users = new ArrayList<>();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void close() {
    	try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateUserList() {
		users.clear();
		try{
			Result<Record> result = create.select().from(USERS).fetch();
			for (Record r : result) {
				Integer id = r.getValue(USERS.ID);
	            String name = r.getValue(USERS.NAME);
	            String pw = r.getValue(USERS.PASSWORD);
	            users.add(new User(id, name, pw));
	        }
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
	}
	
	private static int generateID() {
		return users.size() + 1;
	}
	
	public static void createUser(String name, String pw) {
		int id = generateID();
		String hashed;
		try {
			hashed = PasswordHash.getSaltedHash(pw);
			create.insertInto(USERS, USERS.ID, USERS.NAME, USERS.PASSWORD)
			.values(id, name, hashed)
			.execute();
			updateUserList();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void deleteUser(int id) {
		create.delete(USERS)
	      .where(USERS.ID.equal(id))
	      .execute();
		updateUserList();
	}
	
	public static void updateUser(int id, String newName, String newPassword) {
		try {
			String hashed = PasswordHash.getSaltedHash(newPassword);
			create.update(USERS)
		      .set(USERS.NAME, newName)
		      .set(USERS.PASSWORD, hashed)
		      .where(USERS.ID.equal(id))
		      .execute();
			updateUserList();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static User findUserByName(String name) {
		for(User user : users) {
			if(user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	public static boolean isNameAndPassCorrect(String name, String pw) {
		for(User user : users) {
			try {
				if(user.getName().equals(name) && PasswordHash.passwordCheck(pw, user.getPassword())) {
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}