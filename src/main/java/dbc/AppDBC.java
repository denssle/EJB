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
	            //String url = r.getValue(APPS.URL);
	            //apps.add(new App(id, name, description, url));
	        }
		} catch (DataAccessException e) {
			// TODO: handle exception
		}
	}
}