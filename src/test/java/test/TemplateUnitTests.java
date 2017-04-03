package test;

import static org.junit.Assert.*;

import org.junit.Test;

import bean.App;
import bean.Template;

public class TemplateUnitTests {
	
	@Test
	public void templateTest1() {
	 Template template = new Template(1, "temp");
	  template.addApp(new App(1, "app1", "description", "url", template.getId()));
	  assertEquals(template.getApps().size(), 1);
	}
	
	@Test
	public void templateTest2() {
	 Template template = new Template(1, "temp");
	  assertNotEquals(template.getApps().size(), 1);
	}
	
	@Test
	public void templateTest3() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		template.addApp(app);
		template.check();
		assertTrue(app.isChecked());
	}
	
	@Test
	public void templateTest4() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		app.check();
		template.addApp(app);
		template.check();
		assertTrue(app.isChecked());
	}
	
	@Test
	public void templateTest5() {
		Template template = new Template(1, "temp");
		template.check();
		assertTrue(template.isChecked());
	}
	
	@Test
	public void templateTest6() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		app.check();
		template.addApp(app);
		App app2 = new App(21, "app2", "description", "url", template.getId());
		app2.uncheck();
		template.addApp(app2);
		template.check();
		assertTrue(app2.isChecked());
		assertTrue(app.isChecked());
	}
	
	@Test
	public void templateTest7() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		template.addApp(app);
		template.uncheck();
		assertFalse(app.isChecked());
	}
	
	@Test
	public void templateTest8() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		app.uncheck();
		template.addApp(app);
		template.uncheck();
		assertFalse(app.isChecked());
	}
	
	@Test
	public void templateTest9() {
		Template template = new Template(1, "temp");
		template.uncheck();
		assertFalse(template.isChecked());
	}
	
	@Test
	public void templateTest10() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		app.check();
		template.addApp(app);
		App app2 = new App(21, "app2", "description", "url", template.getId());
		app2.uncheck();
		template.addApp(app2);
		template.uncheck();
		assertFalse(app2.isChecked());
		assertFalse(app.isChecked());
	}
	
	@Test
	public void templateTest11() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		app.check();
		template.addApp(app);
		App app2 = new App(21, "app2", "description", "url", template.getId());
		template.addApp(app2);

		assertFalse(template.isChecked());
	}
	
	@Test
	public void templateTest12() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		template.addApp(app);

		App app2 = new App(21, "app2", "description", "url", template.getId());
		template.addApp(app2);
		app2.check();

		assertFalse(template.isChecked());
	}
	
	@Test
	public void templateTest13() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		template.addApp(app);
		App app2 = new App(21, "app2", "description", "url", template.getId());
		template.addApp(app2);

		assertFalse(template.isChecked());
	}
	
	@Test
	public void templateTest14() {
		Template template = new Template(1, "temp");
		App app = new App(1, "app1", "description", "url", template.getId());
		app.check();
		template.addApp(app);
		App app2 = new App(21, "app2", "description", "url", template.getId());
		template.addApp(app2);
		app2.check();
		
		assertTrue(template.isChecked());
	}
}