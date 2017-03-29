package dbc;

import static org.jooq.h2.generated.Tables.APPS;
import java.util.ArrayList;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import bean.App;

public class AppDBC {
	private static ArrayList<App> apps; 
	private static DSLContext create;
	
	protected static void initAppDBC(DSLContext context) {
		create = context;
		apps = new ArrayList<>();
		updateAppList();
	}
	
	private static void updateAppList() {
		apps.clear();
		try{
			Result<Record> result = create.select().from(APPS).fetch();
			for (Record r : result) {
				Integer id = r.getValue(APPS.ID);
	            String name = r.getValue(APPS.NAME);
	            String description = r.getValue(APPS.DESCRIPTION);
	            //String burl = r.getValue(APPS.URL);
	            apps.add(new App(id, name, description, "DUMMY_URL"));
	        }
			addSystemApps();
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
	}
	
	private static void addSystemApps() {
		apps.add(new App(generateID(), "+", "Create a new app", "/#!/create_app"));
		apps.add(new App(generateID(), "Select Apps", "Choose the apps you want on your launchpad", "/#!/select_apps"));
	}
	
	public static ArrayList<App> getApps() {
		return apps;
	}
	
	private static int generateID() {
		return apps.size() + 1;
	}
	
	public static void createUser(String name, String description, String url) {
		int id = generateID();
		create.insertInto(APPS, APPS.ID, APPS.NAME, APPS.DESCRIPTION)
		.values(id, name, description)
		.execute();
		
		updateAppList();
	}
}