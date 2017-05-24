package controller;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import bean.App;
import bean.User;
import constants.C;
import dbc.UserDBC;

public class UserController extends Controller {
	
	@RequestMapping(value=C.PATH_LOGIN, method = RequestMethod.POST)
	public void login(HttpServletResponse httpResponse, WebRequest request) {
		String username = request.getHeader("username");
		String password = request.getHeader("password");
		System.out.println("login: user: " + username + " password: " + password);
		User user = getUserDBC().isNameAndPassCorrect(username, password);
		if(user == null) {
			redirectToIndex(httpResponse);
		} else {
			redirectToLauchpad(httpResponse, user);
		}
	}
	
	@RequestMapping(value=C.PATH_REGISTER)
	public void register(HttpServletResponse httpResponse, WebRequest request) {
		String username = request.getHeader("username");
		String password = request.getHeader("password");
		System.out.println("reg: user: " + username + " password: " + password);
		UserDBC userDBC = getUserDBC();
		User user = userDBC.findUserByName(username);
		if(user == null) {
			userDBC.createUser(username, password);
			user = userDBC.findUserByName(username);
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
			return getAppDBC().getAppsChecked(getUserDBC().findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	@RequestMapping(value="/getUsers", method = RequestMethod.POST)
	public ArrayList<User> getUsers(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("getUsersAndTemplates auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			return getUserDBC().getUserList();
		} else {
			httpResponse.setStatus(401);
		}
		return null;
	}
	
	@RequestMapping(value="/createUser", method = RequestMethod.POST)
	public void createUser(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("createUser auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String newUserName = request.getHeader("newUserName");
			String newUserPassword = request.getHeader("newUserPassword");
			System.out.println("createUser: " +newUserName + " / " + newUserPassword);
			getUserDBC().createUser(newUserName, newUserPassword);
		} else {
			httpResponse.setStatus(401);
		}
	}
	
	@RequestMapping(value="/updateUser", method = RequestMethod.POST)
	public void updateUser(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("updateUser auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String id = request.getHeader("id");
			String new_name = request.getHeader("new_name");
			System.out.println("updateUser: " +id + " / " + new_name);
			getUserDBC().updateUser(Integer.parseInt(id), new_name);
		} else {
			httpResponse.setStatus(401);
		}
	}
	
	@RequestMapping(value="/deleteUser", method = RequestMethod.POST)
	public void deleteUser(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("deleteUser auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String id = request.getHeader("id");
			System.out.println("deleteUser: " +id);
			getUserDBC().deleteUser(Integer.parseInt(id));
		} else {
			httpResponse.setStatus(401);
		}
	}
}
