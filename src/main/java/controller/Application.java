package controller;

import org.h2.server.web.WebServlet;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import dbc.UserDBC;

@RestController
@EnableAutoConfiguration
public class Application {
	
	@RequestMapping(value="/login",  method = RequestMethod.POST)
	String login(@RequestParam(name ="username", defaultValue="def_username", required=true) String username,
	@RequestParam(name ="password", required=true) String password) {
		System.out.println("login: user: " + username + " password: " + password);
		if(UserDBC.isNameAndPassCorrect(username, password)) {
			return "welcome user: " + username;
		} else {
			return "user not found";
		}
	}
	
	@RequestMapping(value="/register",  method = RequestMethod.POST)
	public String register(@RequestParam(name ="username") String username,
	@RequestParam(name ="password") String password) {
		System.out.println("reg: user: " + username + " password: " + password);
		if(UserDBC.findUserByName(username) == null) {
			UserDBC.createUser(username, password);
			return "welcome user: " + username;
		} else {
			return "user already exists";
		}
	}
	
	// for the h2 database console
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/console/*");
	    return registration;
	}
	
	public static void main(String[] args) throws Exception{
		UserDBC.connect();
		UserDBC.updateUserList();
		// UserDBC.close();
		SpringApplication.run(Application.class, args);
	}
}