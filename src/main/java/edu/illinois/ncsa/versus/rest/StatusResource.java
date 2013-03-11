/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.restlet.Comparison;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 * Status information about service.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/status")
public class StatusResource {

	private static Log log = LogFactory.getLog(StatusResource.class);

	/*@GET
	public String getStatus() {
		return "alive";
	}*/
	@GET
	//@Path("/{id}/status")
	@Produces("text/plain")
	public Response getStatus(@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		
		log.debug("I am inside GET: /status ");
		
		//Response json;
		
		
				
		//String cid = null;
		Collection<Comparison> comparisons = comparisonService.listAll();

		if (comparisons.size() == 0) {
			log.debug("No Comparisons Submitted: FREE");
			return Response.status(200).entity("false").build();
		}
		Iterator<Comparison> itr = comparisons.iterator();
		while (itr.hasNext()) {
			Comparison c = itr.next();
			if(c!=null){
			//if (c.getStatus()== null || c.getStatus()==ComparisonStatus.STARTED){
				if(c.getStatus()!=ComparisonStatus.DONE){
				return Response.status(200).entity("true").build();
		    	}
			}
			}
		 return Response.status(200).entity("false").build();
		}
	
		}
