/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import main.java.edu.illinois.ncsa.versus.census.CensusIndexer;
//import main.java.edu.illinois.ncsa.versus.census.WordspottingExtractor;
import edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter;



import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.IndexingEngine;
import edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.rest.CensusIndexer;
import edu.illinois.ncsa.versus.rest.HashIdSlave;
import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.rest.LinearIndexer;
import edu.illinois.ncsa.versus.rest.SlavesList;
import edu.illinois.ncsa.versus.rest.WordspottingExtractor;

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
		
		
		//LinearIndexer indexer= new LinearIndexer();
		
		//IndexingEngine inengine=new IndexingEngine(indexer,2);
		
		IndexingEngine inengine=new IndexingEngine(2);
		//IndexingEngine inengine=new IndexingEngine(adapter,extractor,indexer,2);    // adding for Indexing
		
		SlavesList slaveList=new SlavesList();  //added for master-slave config
		HashIdSlave compList=new HashIdSlave(); // added for master-slave config
		
		//Index indexHash=new Index();
		
		ServletContext context = event.getServletContext();
		context.setAttribute(CompareRegistry.class.getName(), registry);
		context.setAttribute(ExecutionEngine.class.getName(), engine);
		
		context.setAttribute(IndexingEngine.class.getName(),inengine);
		//context.setAttribute(CensusIndexer.class.getName(),indexer);
		
		context.setAttribute(SlavesList.class.getName(),slaveList); //added for master-slave config
		context.setAttribute(HashIdSlave.class.getName(), compList);
		//context.setAttribute(Index.class.getName(), indexHash);
	}
}
