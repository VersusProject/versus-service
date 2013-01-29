/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Status information about service.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/status")
public class StatusResource {

	private static Log log = LogFactory.getLog(StatusResource.class);

	@GET
	public String getStatus() {
		return "alive";
	}
}
