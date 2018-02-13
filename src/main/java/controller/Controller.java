package controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.h2.server.web.WebServlet;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import bean.User;
import constants.C;
import dbc.AppDBC;
import dbc.DBC;
import dbc.PasswordHash;
import dbc.UserDBC;

@RestController
@EnableAutoConfiguration
public abstract class Controller {
	// for the h2 database console 
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings(C.PATH_CONSOLE);
	    return registration;
	}
		
	boolean authentificateUser(String username, String token) {
		User user = getUserDBC().findUserByName(username);
		try {
			if(user != null && PasswordHash.checkToken(user.getName(), user.getPassword(), token)) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
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
			e.printStackTrace();
		}
	}
	
	void redirectToIndex(HttpServletResponse httpResponse) {
		httpResponse.setStatus(303);
		httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_INDEX);
        try {
            httpResponse.sendRedirect(C.PATH_ANGULAR_INDEX);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	void redirectToLauchpad(HttpServletResponse httpResponse, User user) {
		httpResponse.setStatus(303);
		httpResponse.setHeader(C.LOCATION, C.PATH_ANGULAR_LAUCHPAD);
		addToken(httpResponse, user);
        try {
            httpResponse.sendRedirect(C.PATH_ANGULAR_LAUCHPAD);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	UserDBC getUserDBC() {
		DBC dbc = new DBC();
		DSLContext create = dbc.connect();
		return new UserDBC(create);
	}
	
	AppDBC getAppDBC() {
		DBC dbc = new DBC();
		DSLContext create = dbc.connect();
		return new AppDBC(create);
	}
}
