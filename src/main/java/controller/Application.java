package controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.h2.server.web.WebServlet;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import bean.User;
import constants.C;
import dbc.PasswordHash;
import dbc.UserDBC;

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
			httpResponse.setStatus(303);
			httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_INDEX);
		} else {
			httpResponse.setStatus(303);
			httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_LAUCHPAD);
			addToken(httpResponse, user);
		}
	}
	
	@RequestMapping(value=C.PATH_LOGIN, method = RequestMethod.GET)
	public void login(HttpServletResponse httpResponse, WebRequest request) {
		httpResponse.setStatus(303);
		httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_INDEX);
	}
	
	@RequestMapping(value=C.PATH_ENTER, method = RequestMethod.POST, params = { C.REGISTER })
	public void register(@RequestParam(name =C.USERNAME) String username,
			@RequestParam(name=C.PASSWORD) String password,
			HttpServletResponse httpResponse, WebRequest request) {
		System.out.println("reg: user: " + username + " password: " + password);
		User user = UserDBC.findUserByName(username);
		if(user == null) {
			httpResponse.setStatus(303);
			httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_INDEX);
		} else {
			httpResponse.setStatus(303);
			httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_LAUCHPAD);
			addToken(httpResponse, user);
		}
	}
	
	@RequestMapping(value=C.PATH_LOGOUT)
	public void logout(HttpServletResponse httpResponse, WebRequest request) {
		httpResponse.setStatus(303);
		httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_INDEX);
		httpResponse.addCookie(new Cookie("token", "token"));
	}
	
	@RequestMapping(value="/auth", method = RequestMethod.POST)
	public User auth(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("auth: " +username + " / " + token);
		User user = UserDBC.findUserByName(username);
		try {
			if(user != null && PasswordHash.checkToken(user.getName(), user.getPassword(), token)) {
				return user;
			} else {
				return null;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void addToken(HttpServletResponse httpResponse, User user) {
		try {
			httpResponse.addCookie(new Cookie("token", PasswordHash.createToken(user.getName(), user.getPassword())));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		UserDBC.connect();
		UserDBC.updateUserList();
		// UserDBC.close();
		SpringApplication.run(Application.class, args);
	}
}
