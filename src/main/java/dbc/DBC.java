package dbc;

import java.sql.*;
import org.jooq.*;
import org.jooq.impl.*;
import constants.C;
import dbc.UserDBC;

public class DBC {
	private Connection connection;
	
    public DSLContext connect() {    	
    	try {
			Class.forName(C.DB_DRIVER);
			String url = C.DB_URL;
			String user = C.DB_USER;
			String pw = C.DB_PASSWORD;
			connection = DriverManager.getConnection(url, user, pw);
			DSLContext create = DSL.using(connection, SQLDialect.H2);
			return create;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    public void close() {
    	try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
