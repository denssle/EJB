package bean;

import java.util.ArrayList;

public class Template {
	private int id;
	private String name;
	private Boolean checked;
	private ArrayList<App> apps;
	
	public Template(int id, String name) {
		this.id = id;
		this.name = name;
		this.checked = false;
		this.apps = new ArrayList<App>();
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void check() {
		this.checked = true;
	}
	
	public void uncheck() {
		this.checked = false;
	}
	
	public boolean isChecked() {
		boolean result = true;
		for(App app:apps) {
			if(!app.isChecked()) {
				result = false;
			}
		}
		return result;
	}
	
	public void addApp(App app) {
		this.apps.add(app);
	}
	
	public ArrayList<App> getApps() {
		return this.apps;
	}
}