package bean;

import java.util.Set;
import java.util.TreeSet;

public class User {
	private int id;
    private String name;
    private String password;
    
    public User(int id, String name, String password) {
    	this.id = id; 
        this.name = name;
        this.password = password;
    }

    public int getId() {
    	return id;
    }
    
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

	public Set<Integer> getAppIds() {
		// TODO Auto-generated method stub
		return null;
	}
} 