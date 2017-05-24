package bean;

public class App {
	private int id;
    private String name;
    private String description;
    private String url;
    private Boolean checked;
    
    public App(int id, String name, String description, String url) {
    	this.id = id; 
        this.name = name;
        this.description = description;
        this.url = url;
        this.checked = false;
    }

    public int getId() {
    	return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
    
    public String getUrl() {
    	return this.url;
    }
    
    public void check() {
		this.checked = true;
	}
	
	public void uncheck() {
		this.checked = false;
	}
	
	public boolean isChecked() {
		return this.checked;
	}
}