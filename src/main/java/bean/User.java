package bean;

import java.util.Set;
import java.util.TreeSet;

public class User {
	private int id;
    private String name;
    private String password;
    private String token;
    Set<Integer> appIds;
    
    public User(int id, String name, String password, String token, String appIdsAsString) {
    	this.id = id; 
        this.name = name;
        this.password = password;
        this.token = token;
        this.appIds = new TreeSet<Integer>();
        if(appIdsAsString != null && !appIdsAsString.equals("")) {
        	for (String str : appIdsAsString.split("\\,")) {
        		try {
        			this.appIds.add(Integer.parseInt(str));
        		} catch (NumberFormatException e) {
					// TODO: handle exception
				}
            }
        }
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
    
    public void addAppId(Integer id) {
    	this.appIds.add(id);
    }
    
    public void removeApp(Integer id) {
    	this.appIds.remove(id);
    }
    
    public Set<Integer> getAppIds() {
    	return this.appIds;
    }
    
    public boolean isAppChecked(Integer checkId) {
    	for(Integer id : this.appIds) {
    		if(id.equals(checkId)) {
    			return true;
    		}
    	}
    	return false;
    }
} 