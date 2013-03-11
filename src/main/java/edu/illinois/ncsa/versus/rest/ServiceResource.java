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
				+ "<ul><li><a href='v1/adapters'>Adapters</a></li>"
				+ "<li><a href='v1/extractors'>Extractors</a></li>"
				+ "<li><a href='v1/measures'>Measures</a></li>"
				+ "<li><a href='v1/comparisons'>Comparisons</a></li>"
				+ "<li><a href='v1/slaves'>Slaves</a></li>"
				+ "<li><a href='v1/distributions'>Distributions</a></li>"
				+ "<li><a href='v1/decisionSupport'>DecisionSupport</a></li>"
				+"<li><a href='v1/multiLabelDecisionSupport'>MultiLabelDecisionSupport</a></li>"
				+ "</ul>");
	}
}
