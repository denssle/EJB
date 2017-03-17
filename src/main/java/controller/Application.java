package controller;

import org.h2.server.web.WebServlet;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import bean.User;
import dbc.UserDBC;

@RestController
@EnableAutoConfiguration
public class Application {
	
	@RequestMapping(value="/login",  method = RequestMethod.POST)
	String login(@RequestParam(name ="username", defaultValue="def_username", required=true) String username,
	@RequestParam(name ="password", required=true) String password) {
		System.out.println("login: user: " + username + " password: " + password);
		return "login";
	}
	
	@RequestMapping(value="/register",  method = RequestMethod.POST)
	public User register(@RequestParam(name ="username") String username,
	@RequestParam(name ="password") String password) {
		System.out.println("reg: user: " + username + " password: " + password);
		return new User(username, password);
	}
	
	// for the h2 console
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/console/*");
	    return registration;
	}
	
	public static void main(String[] args) throws Exception{
		UserDBC.connect();
		UserDBC.showDB();
		UserDBC.close();
		SpringApplication.run(Application.class, args);
	}
}
