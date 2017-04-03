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
		for(App app: apps) {
			app.check();
		}
	}
	
	public void uncheck() {
		this.checked = false;
		for(App app: apps) {
			app.uncheck();
		}
	}
	
	public boolean isChecked() {
		this.checked = true;
		for(App app:apps) {
			if(!app.isChecked()) {
				this.checked = false;
			}
		}
		return this.checked;
	}
	
	public void addApp(App app) {
		this.apps.add(app);
	}
	
	public ArrayList<App> getApps() {
		return this.apps;
	}
}