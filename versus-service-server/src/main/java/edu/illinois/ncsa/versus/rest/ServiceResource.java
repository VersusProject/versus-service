/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * List available endpoints.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/")
public class ServiceResource {

	@GET
	public String listServices() {
		return new String("<h3>Versus</h3>"
				+ "<ul><li><a href='adapters'>Adapters</a></li>"
				+ "<li><a href='extractors'>Extractors</a></li>"
				+ "<li><a href='measures'>Measures</a></li>"
				+ "<li><a href='comparisons'>Comparisons</a></li>"
				+ "<li><a href='slaves'>Slaves</a></li>"
				+ "<li><a href='distributions'>Distributions</a></li>"
				+ "<li><a href='decisionSupport'>DecisionSupport</a></li>"
				+ "</ul>");
	}
}
