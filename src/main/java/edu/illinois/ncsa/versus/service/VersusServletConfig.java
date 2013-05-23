/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.IndexingEngine;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.rest.FileMap;

import edu.illinois.ncsa.versus.rest.HashIdSlave;
//import edu.illinois.ncsa.versus.rest.IdMap;
import edu.illinois.ncsa.versus.rest.RankSlaves;
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
		context.removeAttribute(SlavesList.class.getName()); // added for
																// master-slave
																// config
		context.removeAttribute(HashIdSlave.class.getName());// added for
																// master-slave
																// config
		//context.removeAttribute(HashCompareID.class.getName());
		context.removeAttribute(FileMap.class.getName());
		
		context.removeAttribute(RankSlaves.class.getName());
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		CompareRegistry registry = new CompareRegistry();
		ExecutionEngine engine = new ExecutionEngine();

		

		IndexingEngine inengine = new IndexingEngine(2);
		

		SlavesList slaveList = new SlavesList(); // added for master-slave
													// config
		
		HashIdSlave compList=new HashIdSlave(); 
		//HashCompareID compList=new HashCompareID();//
		FileMap fmap=new FileMap();                 //modified master-slave    
		
		RankSlaves rank= new RankSlaves();   //rank RR

		ServletContext context = event.getServletContext();
		
		context.setAttribute(CompareRegistry.class.getName(), registry);
		context.setAttribute(ExecutionEngine.class.getName(), engine);

		context.setAttribute(IndexingEngine.class.getName(), inengine);
		       
		context.setAttribute(SlavesList.class.getName(), slaveList); // added
																		// for
																		// master-slave
																		// config
		
		//context.setAttribute(HashCompareID.class.getName(), compList);
		context.setAttribute(HashIdSlave.class.getName(), compList);
		context.setAttribute(FileMap.class.getName(), fmap);
		
		context.setAttribute(RankSlaves.class.getName(), rank);
	}
	
}
