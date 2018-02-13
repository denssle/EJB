package dbc;

import static org.jooq.h2.generated.Tables.USERS;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import bean.Template;
import bean.User;

public class UserDBC {
	private DSLContext create;
	
	public UserDBC(DSLContext context) {
		this.create = context;
	}
	
	public ArrayList<User> getUserList() {
		ArrayList<User> users = new ArrayList<>();
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
		return users;
	}
	
	private int generateID() {
		ArrayList<User> users = getUserList();
		return users.get(users.size()-1).getId() + 1;
	}
	
	public void createUser(String name, String pw) {
		int id = generateID();
		String hashed;
		try {
			hashed = PasswordHash.getSaltedHash(pw);
			create.insertInto(USERS, USERS.ID, USERS.NAME, USERS.PASSWORD)
			.values(id, name, hashed)
			.execute();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteUser(int id) {
		create.delete(USERS)
	      .where(USERS.ID.equal(id))
	      .execute();
	}
	
	public void updateUser(int id, String newName) {
		create.update(USERS)
		  .set(USERS.NAME, newName)
		  .where(USERS.ID.equal(id))
		  .execute();
	}
	
	public User findUserByName(String username) {
		Record r = create.select().from(USERS).where(USERS.NAME.equal(username)).fetchOne();
		if( r != null ) {
			Integer userid = r.getValue(USERS.ID);
			String name = r.getValue(USERS.NAME);
			String pw = r.getValue(USERS.PASSWORD);
			return(new User(userid, name, pw));
		}
		return null;
	}
	
	public User isNameAndPassCorrect(String name, String pw) {
		ArrayList<User> users = getUserList();
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
	
	public void checkApp(String username, String appId) {
		String result = "";
		User user = findUserByName(username);
		/*
		if(user.isAppChecked(Integer.parseInt(appId))) { // remove app
			user.removeApp(Integer.parseInt(appId));
		} else { // check app
			user.addAppId(Integer.parseInt(appId));
		}
		for(Integer i : user.getAppIds()) {
			result = result+i+",";
		}
		*/
		updateUserAppList(user.getId(), result);
	}
	
	public void checkTemplate(String username, Template template) {
		String result = "";
		User user = findUserByName(username);
		/*
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
		*/
		updateUserAppList(user.getId(), result);
	}
	
	private void updateUserAppList(Integer id, String result) {
		/*
		System.out.println("new applist: " + result);
		create.update(USERS)
	      .set(USERS.APPS, result)
	      .where(USERS.ID.equal(id))
	      .execute();
	      */
	}
}
