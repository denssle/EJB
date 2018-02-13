package dbc;

import static org.jooq.h2.generated.Tables.APPS;
import static org.jooq.h2.generated.Tables.APPTEMPLATE;
import static org.jooq.h2.generated.Tables.USERS;
import static org.jooq.h2.generated.Tables.TEMPLATES;
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
	private DSLContext create;
	
	public AppDBC(DSLContext context) {
		this.create = context;
	}
	
	private ArrayList<App> getApps() {
		ArrayList<App> apps = new ArrayList<>();
		try{
			Result<Record> result = create.select().from(APPS).fetch();
			for (Record r : result) {
				Integer id = r.getValue(APPS.ID);
	            String name = r.getValue(APPS.NAME);
	            String description = r.getValue(APPS.DESCRIPTION);
	            String burl = r.getValue(APPS.LINK);
	            App app = new App(id, name, description, burl);
	            apps.add(app);
	        }
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
		return apps;
	}
	
	public ArrayList<App> getAppsChecked(Set<Integer> checkAppIds) {
		ArrayList<App> result = new ArrayList<>();
		for(Integer id : checkAppIds) {
			App app = getApp(id);
			if(app != null) {
				app.check();
				result.add(app);
			}
		}
		result.add(new App(generateAppID(), "+", "Create a new app", "/#!/createapp"));
		result.add(new App(generateAppID()+1, "Select Apps", "Choose the apps you want on your launchpad", "/#!/selectapps"));
		result.add(new App(generateAppID()+2, "manager", "update apps and users", "/#!/manager"));
		return result;
	}
	
	private ArrayList<Template> getTemplates() {
		ArrayList<Template> templates = new ArrayList<>();
		/*
		ArrayList<App> apps = this.getApps();
		try{
			Result<Record> result = create.select().from(TEMPLATES).fetch();
			for (Record r : result) {
				Integer id = r.getValue(TEMPLATES.ID);
	            String name = r.getValue(TEMPLATES.NAME);
	            Template template = new Template(id, name);
	            for(App app: apps) {
	            	if(id.equals(app.getTemplate())) {
	            		template.addApp(app);
	            	}
	            }
	            templates.add(template);
	            
	        }
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
		*/
		return templates;
	}
	
	public Template getTemplate(Integer id) {
		Record r = create.select().from(TEMPLATES).where(TEMPLATES.ID.equal(id)).fetchOne();
		int tempId = r.getValue(TEMPLATES.ID);
        String tempName = r.getValue(TEMPLATES.NAME);
		return new Template(tempId, tempName);
	}
	
	public App getApp(Integer id) {
		Record r = create.select().from(APPS).where(APPS.ID.equal(id)).fetchOne();
		Integer appid = r.getValue(APPS.ID);
        String name = r.getValue(APPS.NAME);
        String description = r.getValue(APPS.DESCRIPTION);
        String burl = r.getValue(APPS.LINK);
        App app = new App(appid, name, description, burl);
		return app;
	}
	
	private int generateAppID() {
		ArrayList<App> apps = this.getApps();
		return apps.get(apps.size()-1).getId() + 1;
	}
	
	private int generateTemplateID() {
		ArrayList<Template> templates = this.getTemplates();
		return templates.get(templates.size()-1).getId() + 1;
	}
	
	public void createApp(String name, String description, String url) {
		int id = generateAppID();
		create.insertInto(APPS, APPS.ID, APPS.NAME, APPS.DESCRIPTION, APPS.LINK)
		.values(id, name, description, url)
		.execute();
	}
	
	public void updateApp(int id, String name, String description, String url) {
		create.update(APPS)
		  .set(APPS.NAME, name)
		  .set(APPS.DESCRIPTION, description)
		  .set(APPS.LINK, url)
		  .where(APPS.ID.equal(id))
		  .execute();
	}
	
	public ArrayList<Template> getTemplates(Set<Integer> appIds) {
		System.out.println("get templates: "+ appIds);
		ArrayList<Template> templates = this.getTemplates();
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

	public void createTemplate(String new_template_name) {
		create.insertInto(TEMPLATES, TEMPLATES.ID, TEMPLATES.NAME)
		.values(generateTemplateID(), new_template_name)
		.execute();
	}
	
	public void updateTemplate(String new_name, int id) {
		create.update(TEMPLATES)
		  .set(TEMPLATES.NAME, new_name)
		  .where(TEMPLATES.ID.equal(id))
		  .execute();
	}
	
	public void deleteTemplate(int id) {
		create.delete(TEMPLATES)
	      .where(TEMPLATES.ID.equal(id))
	      .execute();
		/*
		create.delete(APPS)
	      .where(APPS.TEMPLATE.equal(id))
	      .execute();
	      */
	}
}