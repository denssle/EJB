package controller;

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
import dbc.UserDBC;

@RestController
@EnableAutoConfiguration
public class Application {
	
	@RequestMapping(value=C.PATH_ENTER,  method = RequestMethod.POST, params = { C.LOGIN })
	public User login(@RequestParam(name =C.USERNAME, required=true) String username,
	@RequestParam(name =C.PASSWORD, required=true) String password,
	HttpServletResponse httpResponse, WebRequest request) {
		System.out.println("login: user: " + username + " password: " + password);
		httpResponse.setStatus(303);
		httpResponse.setHeader("Location", "/#!/lauchpad");
		return UserDBC.isNameAndPassCorrect(username, password);
	}
	
	@RequestMapping(value=C.PATH_ENTER,  method = RequestMethod.POST, params = { C.REGISTER })
	public User register(@RequestParam(name =C.USERNAME) String username,
	@RequestParam(name =C.PASSWORD) String password) {
		System.out.println("reg: user: " + username + " password: " + password);
		return UserDBC.findUserByName(username);
	}
	
	// for the h2 database console
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings(C.PATH_CONSOLE);
	    return registration;
	}
	
	public static void main(String[] args) throws Exception{
		UserDBC.connect();
		UserDBC.updateUserList();
		// UserDBC.close();
		SpringApplication.run(Application.class, args);
	}
}
