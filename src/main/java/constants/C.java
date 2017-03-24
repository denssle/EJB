package constants;

public class C {
	public static final String PATH_ENTER = "/enter";
	public static final String PATH_LOGIN = "/login";
	public static final String PATH_CONSOLE = "/console/*";
	public static final String PATH_LOGOUT = "/logout";
	public static final String PATH_ANGULAR_INDEX = "/#!/";
	public static final String PATH_ANGULAR_LAUCHPAD = "/#!/lauchpad";
	public static final String LOGIN = "login";
	public static final String REGISTER = "register";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String LOCATION = "Location";
	
	public static final String DB_DRIVER = "org.h2.Driver";
	public static final String DB_URL = "jdbc:h2:~/test";
	public static final String DB_USER = "sa";
	public static final String DB_PASSWORD = "";
	
	public static final String SALT = "SHA1PRNG";
	public static final String HASH = "PBKDF2WithHmacSHA1";
	public static final String SEPERATOR = "§";
	public static final String HASH_REG_EXPR = "\\§";
}