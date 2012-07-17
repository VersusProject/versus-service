/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * @author lmarini
 * 
 */
public class VersusServerResource extends ServerResource {

	@Override
	@Get
	public Representation get() {
		String content = new String("<h3>Versus</h3>"
				+ "<ul><li><a href='/versus/api/adapters'>Adapters</a></li>"
				+ "<li><a href='/versus/api/extractors'>Extractors</a></li>"
				+ "<li><a href='/versus/api/measures'>Measures</a></li>"
				+ "<li><a href='/versus/api/comparisons'>Comparisons</a></li>"
				+ "<li><a href='/versus/api/slaves'>Slaves</a></li>"
				+ "<li><a href='/versus/api/distributions'>Distributions</a></li>" 
				+ "<li><a href='/versus/api/decisionSupport'>DecisionSupport</a></li>" 
				+ "<li><a href='/versus/api/multiLabelDecisionSupport'>MultiLabelDecisionSupport</a></li>" 
				+ "</ul>");
		Representation representation = new StringRepresentation(content,
				MediaType.TEXT_HTML);
		return representation;
	}
}
