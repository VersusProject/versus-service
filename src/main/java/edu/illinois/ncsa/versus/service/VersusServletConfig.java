/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.rest.HashIdSlave;
import edu.illinois.ncsa.versus.rest.SlavesList;

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
		context.removeAttribute(SlavesList.class.getName()); //added for master-slave config
		context.removeAttribute(HashIdSlave.class.getName());//added for master-slave config
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		CompareRegistry registry = new CompareRegistry();
		ExecutionEngine engine = new ExecutionEngine();
		SlavesList slaveList=new SlavesList();  //added for master-slave config
		HashIdSlave compList=new HashIdSlave(); // added for master-slave config
		ServletContext context = event.getServletContext();
		context.setAttribute(CompareRegistry.class.getName(), registry);
		context.setAttribute(ExecutionEngine.class.getName(), engine);
		context.setAttribute(SlavesList.class.getName(),slaveList); //added for master-slave config
		context.setAttribute(HashIdSlave.class.getName(), compList);
	}
}
