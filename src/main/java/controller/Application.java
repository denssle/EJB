package controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.h2.server.web.WebServlet;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import bean.App;
import bean.Template;
import bean.User;
import constants.C;
import dbc.PasswordHash;
import dbc.UserDBC;
import dbc.AppDBC;
import dbc.DBC;

@RestController
@EnableAutoConfiguration
public class Application {
	@RequestMapping(value=C.PATH_ENTER, method = RequestMethod.POST, params = { C.LOGIN })
	public void enter(@RequestParam(name =C.USERNAME, required=true) String username,
	@RequestParam(name =C.PASSWORD, required=true) String password,
	HttpServletResponse httpResponse, WebRequest request) {
		System.out.println("login: user: " + username + " password: " + password);
		User user = UserDBC.isNameAndPassCorrect(username, password);
		if(user == null) {
			redirectToIndex(httpResponse);
		} else {
			redirectToLauchpad(httpResponse, user);
		}
	}
	
	@RequestMapping(value=C.PATH_LOGIN, method = RequestMethod.GET)
	public void login(HttpServletResponse httpResponse, WebRequest request) {
		redirectToIndex(httpResponse);
	}
	
	@RequestMapping(value=C.PATH_ENTER, method = RequestMethod.POST, params = { C.REGISTER })
	public void register(@RequestParam(name =C.USERNAME) String username,
			@RequestParam(name=C.PASSWORD) String password,
			HttpServletResponse httpResponse, WebRequest request) {
		System.out.println("reg: user: " + username + " password: " + password);
		User user = UserDBC.findUserByName(username);
		if(user == null) {
			UserDBC.createUser(username, password);
			user = UserDBC.findUserByName(username);
			redirectToLauchpad(httpResponse, user);
		} else {
			redirectToIndex(httpResponse);
		}
	}
	
	@RequestMapping(value=C.PATH_LOGOUT)
	public void logout(HttpServletResponse httpResponse, WebRequest request) {
		redirectToIndex(httpResponse);
		httpResponse.addCookie(new Cookie("token", "token"));
	}
	
	@RequestMapping(value="/auth", method = RequestMethod.POST)
	public ArrayList<App> auth(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			return AppDBC.getApps(UserDBC.findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	@RequestMapping(value="/getTemplates", method = RequestMethod.POST)
	public ArrayList<Template> getTemplats(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("getTemplates auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			return AppDBC.getTemplates(UserDBC.findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	@RequestMapping(value="/createapp", method = RequestMethod.POST)
	public void createApp(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("createapp: " +request);
		if(authentificateUser(username, token)) {
			String app_name = request.getHeader("app_name");
			String app_url = request.getHeader("app_url");
			String app_description = request.getHeader("app_description");
			Integer app_template_id = Integer.parseInt(request.getHeader("app_template_id"));
			System.out.println("createapp "+app_name+" "+app_url+" "+app_description+" "+app_template_id);
			AppDBC.createApp(app_name, app_description, app_url, app_template_id);
			redirectToLauchpad(httpResponse, UserDBC.findUserByName(username));
		}
	}
	
	@RequestMapping(value="/checkApp", method = RequestMethod.POST)
	public ArrayList<Template> checkApp(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		String appid = request.getHeader("appid");
		System.out.println("checkApp auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			UserDBC.checkApp(username, appid);
			return AppDBC.getTemplates(UserDBC.findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	@RequestMapping(value="/checkTemplate", method = RequestMethod.POST)
	public ArrayList<Template> checkTemplate(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		String templateId = request.getHeader("templateId");
		System.out.println("checkTemplate auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			UserDBC.checkTemplate(username, AppDBC.getTemplate(Integer.parseInt(templateId)));
			return AppDBC.getTemplates(UserDBC.findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	// for the h2 database console
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings(C.PATH_CONSOLE);
	    return registration;
	}
	
	private boolean authentificateUser(String username, String token) {
		User user = UserDBC.findUserByName(username);
		try {
			if(user != null && PasswordHash.checkToken(user.getName(), user.getPassword(), token)) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private void addToken(HttpServletResponse httpResponse, User user) {
		try {
			Cookie token_cookie = new Cookie("token", PasswordHash.createToken(user.getName(), user.getPassword()));
			token_cookie.setMaxAge(5*24*60*60*1000);
			httpResponse.addCookie(token_cookie);
			
			Cookie username_cookie = new Cookie("username", user.getName());
			username_cookie.setMaxAge(5*24*60*60*1000);
			httpResponse.addCookie(username_cookie);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void redirectToIndex(HttpServletResponse httpResponse) {
		httpResponse.setStatus(303);
		httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_INDEX);
	}
	
	private void redirectToLauchpad(HttpServletResponse httpResponse, User user) {
		httpResponse.setStatus(303);
		httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_LAUCHPAD);
		addToken(httpResponse, user);
	}
	
	public static void main(String[] args) {
		DBC.connect();
		// UserDBC.close();
		SpringApplication.run(Application.class, args);
	}
}
