/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * Setup FreeMarker for templating.
 * 
 * @author Luigi Marini
 * 
 */
public class FreeMarkerServletConfig implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		context.removeAttribute(Configuration.class.getName());
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Configuration cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(event.getServletContext(),
				"WEB-INF/templates");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		ServletContext context = event.getServletContext();
		context.setAttribute(Configuration.class.getName(), cfg);
	}

}
