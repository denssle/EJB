package dbc;

import static org.jooq.h2.generated.Tables.APPS;
import static org.jooq.h2.generated.Tables.APPTEMPLATES;
import static org.jooq.h2.generated.Tables.USERS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import bean.App;
import bean.Template;
import ch.qos.logback.core.net.SyslogOutputStream;

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
	            Template template = getTemplateForId(template_id);
	            App app = new App(id, name, description, burl, template_id);
	            apps.add(app);
	            template.addApp(app);
	        }
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
	
	public static App getApp(Integer id) {
		for(App app : apps) {
			if(id.equals(app.getId())) {
				return app;
			}
		}
		return null;
	}
	
	public static Template getTemplate(Integer id) {
		for(Template template : templates) {
			if(id.equals(template.getId())) {
				return template;
			}
		}
		return null;
	}
	
	public static ArrayList<App> getApps(Set<Integer> checkAppIds) {
		ArrayList<App> result = new ArrayList<>();
		for(Integer id : checkAppIds) {
			App app = getApp(id);
			if(app != null) {
				app.check();
				result.add(app);
			}
		}
		result.add(new App(generateAppID(), "+", "Create a new app", "/#!/createapp", 0));
		result.add(new App(generateAppID()+1, "Select Apps", "Choose the apps you want on your launchpad", "/#!/selectapps", 0));
		result.add(new App(generateAppID()+2, "manager", "update apps and users", "/#!/manager", 0));
		return result;
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
	
	public static ArrayList<Template> getTemplates(Set<Integer> appIds) {
		updateAppList();
		System.out.println("get templates: "+ appIds);
		for(Template template : templates) {
			for(App app: template.getApps()) {
				for(Integer checkedId : appIds) {
					if(checkedId.equals(app.getId())) {
						System.out.println("checked app: " + app.getName());
						app.check();
					}
				}
			}
			template.isChecked();
		}
		return templates;
	}

	public static void createTemplate(String new_template_name) {
		create.insertInto(APPTEMPLATES, APPTEMPLATES.ID, APPTEMPLATES.NAME)
		.values(generateTemplateID(), new_template_name)
		.execute();
		
		updateAppList();
	}
	
	public static void deleteTemplate(int id) {
		create.delete(APPTEMPLATES)
	      .where(APPTEMPLATES.ID.equal(id))
	      .execute();
		
		create.delete(APPS)
	      .where(APPS.TEMPLATE.equal(id))
	      .execute();
		updateAppList();
	}
}