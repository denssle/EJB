package controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import bean.Template;
import dbc.AppDBC;
import dbc.UserDBC;

public class TemplateController extends Controller{
	@RequestMapping(value="/getTemplates", method = RequestMethod.POST)
	public ArrayList<Template> getTemplats(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("getTemplates auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			return getAppDBC().getTemplates(getUserDBC().findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	@RequestMapping(value="/checkTemplate", method = RequestMethod.POST)
	public ArrayList<Template> checkTemplate(HttpServletResponse httpResponse, WebRequest request) {
		AppDBC appDBC = getAppDBC();
		UserDBC userDBC = getUserDBC();
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		String templateId = request.getHeader("templateId");
		System.out.println("checkTemplate auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			userDBC.checkTemplate(username, appDBC.getTemplate(Integer.parseInt(templateId)));
			return appDBC.getTemplates(userDBC.findUserByName(username).getAppIds());
		} else {
			httpResponse.setStatus(401);
			return null;
		}
	}
	
	@RequestMapping(value="/createTemplate", method = RequestMethod.POST)
	public void createTemplate(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("createTemplate auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String newTemplateName = request.getHeader("newTemplateName");
			System.out.println("createTemplate: " + newTemplateName);
			getAppDBC().createTemplate(newTemplateName);
		} else {
			httpResponse.setStatus(401);
		}
	}
	
	@RequestMapping(value="/updateTemplate", method = RequestMethod.POST)
	public void updateTemplate(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("updateTemplate auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String new_name = request.getHeader("new_name");
			String id = request.getHeader("id");
			System.out.println("updateTemplate: " +new_name);
			getAppDBC().updateTemplate(new_name, Integer.parseInt(id));
		} else {
			httpResponse.setStatus(401);
		}
	}
	
	@RequestMapping(value="/deleteTemplate", method = RequestMethod.POST)
	public void deleteTemplate(HttpServletResponse httpResponse, WebRequest request) {
		String token = request.getHeader("token");
		String username = request.getHeader("username");
		System.out.println("deleteTemplate auth: " +username + " / " + token);
		if(authentificateUser(username, token)) {
			String id = request.getHeader("id");
			System.out.println("deleteTemplate: " +id);
			getAppDBC().deleteTemplate(Integer.parseInt(id));
		} else {
			httpResponse.setStatus(401);
		}
	}
}
