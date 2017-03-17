package dbc;

import static org.jooq.h2.generated.Tables.USERS;
import static org.jooq.impl.DSL.*;

import java.sql.*;
import org.jooq.*;
import org.jooq.impl.*;

public class UserDBC {
	private static Connection connection;
	private static DSLContext create;
	
    public static void connect() throws Exception {
    	Class.forName("org.h2.Driver");
    	String url = "jdbc:h2:~/test";
		String user = "sa";
		String pw = "";
		connection = DriverManager.getConnection(url, user, pw);
		create = DSL.using(connection, SQLDialect.H2);
    }
    
    public static void close() throws Exception {
    	connection.close();
	}
	
	public static void showDB() {
		Result<Record> result = create.select().from(USERS).fetch();
		System.out.println(result);
		for (Record r : result) {
			Integer id = r.getValue(USERS.ID);
            String name = r.getValue(USERS.NAME);
            String pw = r.getValue(USERS.PASSWORD);

            System.out.println("ID: " + id + " name: " + name + " pw: " + pw);
        }
	}
}