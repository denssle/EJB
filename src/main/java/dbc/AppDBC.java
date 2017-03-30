package dbc;

import static org.jooq.h2.generated.Tables.APPS;
import static org.jooq.h2.generated.Tables.APPTEMPLATES;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import bean.App;
import bean.Template;

public class AppDBC {
	private static ArrayList<App> apps; 
	private static ArrayList<Template> templates; 
	private static DSLContext create;
	
	protected static void initAppDBC(DSLContext context) {
		create = context;
		apps = new ArrayList<>();
		templates = new ArrayList<>();
		updateAppList();
	}
	
	private static void updateAppList() {
		updateTemplateList();
		apps.clear();
		try{
			Result<Record> result = create.select().from(APPS).fetch();
			for (Record r : result) {
				Integer id = r.getValue(APPS.ID);
	            String name = r.getValue(APPS.NAME);
	            String description = r.getValue(APPS.DESCRIPTION);
	            String burl = r.getValue(APPS.LINK);
	            Integer template_id = r.getValue(APPS.TEMPLATE);
	            apps.add(new App(id, name, description, burl, getTemplateForId(template_id)));
	        }
			addSystemApps();
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
	}
	
	private static void updateTemplateList() {
		templates.clear();
		try{
			Result<Record> result = create.select().from(APPTEMPLATES).fetch();
			for (Record r : result) {
				Integer id = r.getValue(APPTEMPLATES.ID);
	            String name = r.getValue(APPTEMPLATES.NAME);
	            templates.add(new Template(id, name));
	        }
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
	}
	
	private static Template getTemplateForId(Integer id) {
		for(Template template : templates) {
			if(id.equals(template.getId())) {
				return template;
			}
		}
		return null;
	}
	
	private static void addSystemApps() {
		Template systemTemplate = new Template(generateTemplateID(), "Systemapps");
		apps.add(new App(generateAppID(), "+", "Create a new app", "/#!/createapp", systemTemplate));
		apps.add(new App(generateAppID(), "Select Apps", "Choose the apps you want on your launchpad", "/#!/selectapps", systemTemplate));
	}
	
	public static ArrayList<App> getApps() {
		return apps;
	}
	
	private static int generateAppID() {
		return apps.get(apps.size()-1).getId() + 1;
	}
	
	private static int generateTemplateID() {
		return templates.get(templates.size()-1).getId() + 1;
	}
	
	public static void createApp(String name, String description, String url, int templateId) {
		int id = generateAppID();
		create.insertInto(APPS, APPS.ID, APPS.NAME, APPS.DESCRIPTION, APPS.LINK, APPS.TEMPLATE)
		.values(id, name, description, url, templateId)
		.execute();
		
		updateAppList();
	}
	
	public static ArrayList<Template> getTemplates() {
		return templates;
	}
}