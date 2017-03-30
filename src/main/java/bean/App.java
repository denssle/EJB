package bean;

public class App {
	private int id;
    private String name;
    private String description;
    private String url;
    private Template template;
    
    public App(int id, String name, String description, String url, Template template) {
    	this.id = id; 
        this.name = name;
        this.description = description;
        this.url = url;
        this.template = template;
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
    
    public Template getTemplate() {
    	return this.template;
    }
}