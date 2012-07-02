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
		return new String(
				"<h3>Versus</h3>"
						+ "<ul><li><a href='/versus/api/adapters'>Adapters</a></li>"
						+ "<li><a href='/versus/api/extractors'>Extractors</a></li>"
						+ "<li><a href='/versus/api/measures'>Measures</a></li>"
						+ "<li><a href='/versus/api/comparisons'>Comparisons</a></li>"
						+ "<li><a href='/versus/api/slaves'>Slaves</a></li>"
						+ "<li><a href='/versus/api/distributions'>Distributions</a></li>"
						+ "<li><a href='/versus/api/decisionSupport'>DecisionSupport</a></li>"
						+ "</ul>");
	}
}
