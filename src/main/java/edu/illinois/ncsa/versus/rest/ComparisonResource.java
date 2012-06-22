/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

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

import edu.illinois.ncsa.versus.restlet.Comparison;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 * Submit and query comparisons.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/comparisons")
public class ComparisonResource {

	private static Log log = LogFactory.getLog(ComparisonResource.class);

	@GET
	public String list() {
		return "list";
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getComparison(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context
				.getAttribute(Injector.class.getName());

		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		Comparison c = comparisonService.getComparison(id);
		System.out.println("Comparison " + c);
		if (c == null) {
			log.debug("Comparison not found " + id);
			return Response.status(404)
					.entity("Comparison " + id + " not found.").build();
		} else {
			log.debug("Comparison found " + id);
			return Response.status(200).entity(c).build();
		}
	}
}
