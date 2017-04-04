package dbc;

import static org.jooq.h2.generated.Tables.USERS;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import bean.App;
import bean.Template;
import bean.User;

public class UserDBC {
	private static ArrayList<User> users; 
	private static DSLContext create;
	
	protected static void initUserDBC(DSLContext context) {
		create = context;
		users = new ArrayList<>();
		updateUserList();
	}
	
	private static void updateUserList() {
		users.clear();
		try{
			Result<Record> result = create.select().from(USERS).fetch();
			for (Record r : result) {
				Integer id = r.getValue(USERS.ID);
	            String name = r.getValue(USERS.NAME);
	            String pw = r.getValue(USERS.PASSWORD);
	            String token = r.getValue(USERS.TOKEN);
	            String appIds = r.getValue(USERS.APPS);
	            users.add(new User(id, name, pw, token, appIds));
	        }
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
	}
	
	private static int generateID() {
		return users.get(users.size()-1).getId() + 1;
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
	
	public static void updateUser(int id, String newName) {
		create.update(USERS)
		  .set(USERS.NAME, newName)
		  .where(USERS.ID.equal(id))
		  .execute();
		updateUserList();
	}
	
	public static User findUserByName(String name) {
		for(User user : users) {
			if(user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	public static User isNameAndPassCorrect(String name, String pw) {
		for(User user : users) {
			try {
				if(user.getName().equals(name) && PasswordHash.passwordCheck(pw, user.getPassword())) {
					return user;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void checkApp(String username, String appId) {
		String result = "";
		User user = findUserByName(username);
		if(user.isAppChecked(Integer.parseInt(appId))) { // remove app
			user.removeApp(Integer.parseInt(appId));
		} else { // check app
			user.addAppId(Integer.parseInt(appId));
		}
		for(Integer i : user.getAppIds()) {
			result = result+i+",";
		}
		updateUserAppList(user.getId(), result);
	}
	
	public static void checkTemplate(String username, Template template) {
		String result = "";
		User user = findUserByName(username);
		if(template.isChecked()) {
			template.uncheck();
			for(App app: template.getApps()) {
				user.removeApp(app.getId());
			}
		} else {
			template.check();
			for(App app: template.getApps()) {
				user.addAppId(app.getId());
			}
		}
		for(Integer i : user.getAppIds()) {
			result = result+i+",";
		}
		updateUserAppList(user.getId(), result);
	}
	
	private static void updateUserAppList(Integer id, String result) {
		System.out.println("new applist: " + result);
		create.update(USERS)
	      .set(USERS.APPS, result)
	      .where(USERS.ID.equal(id))
	      .execute();
		updateUserList();
	}
	
	public static ArrayList<User> getUsers() {
		return users;
	}
}
