/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.SlavesManager;

/**
 * Load Versus registry in servlet context.
 * 
 * @author Luigi Marini
 * 
 */
public class VersusServletConfig implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		context.removeAttribute(CompareRegistry.class.getName());
		// TODO nicely shutdown engine
		context.removeAttribute(ExecutionEngine.class.getName());
        context.removeAttribute(SlavesManager.class.getName());
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		CompareRegistry registry = new CompareRegistry();
		ExecutionEngine engine = new ExecutionEngine();
        SlavesManager slavesManager = new SlavesManager();
		ServletContext context = event.getServletContext();
		context.setAttribute(CompareRegistry.class.getName(), registry);
		context.setAttribute(ExecutionEngine.class.getName(), engine);
        context.setAttribute(SlavesManager.class.getName(), slavesManager);
	}
}
