package controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import bean.Template;

public class AppController extends Controller {
	@RequestMapping(value="/createapp", method = RequestMethod.POST)
	public void createApp(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("createapp: " +request);
		if(authentificateUser(username, token)) {
			String app_name = request.getHeader("app_name");
			String app_url = request.getHeader("app_url");
			String app_description = request.getHeader("app_description");
			System.out.println("createapp "+app_name+" "+app_url+" "+app_description);
			getAppDBC().createApp(app_name, app_description, app_url);
			redirectToLauchpad(httpResponse, getUserDBC().findUserByName(username));
		}
	}
	
	@RequestMapping(value="/checkApp", method = RequestMethod.POST)
	public ArrayList<Template> checkApp(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		String appid = request.getHeader("appid");
		System.out.println("checkApp auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			getUserDBC().checkApp(username, appid);
			return getAppDBC().getTemplates(getUserDBC().findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	@RequestMapping(value="/updateApp", method = RequestMethod.POST)
	public void updateApp(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("updateApp auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			Integer id = Integer.parseInt(request.getHeader("id"));
			String name = request.getHeader("name");
			String description = request.getHeader("description");
			String url = request.getHeader("url");
			System.out.println("updateApp: " + id + " name " + name + " description " + description + " url " + url);
			getAppDBC().updateApp(id, name, description, url);
		} else {
			httpResponse.setStatus(401);
		}
	}
	
	@RequestMapping(value="/deleteApp", method = RequestMethod.POST)
	public void deleteApp(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("deleteApp auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String id = request.getHeader("id");
			System.out.println("deleteApp: " +id);
		} else {
			httpResponse.setStatus(401);
		}
	}
}
