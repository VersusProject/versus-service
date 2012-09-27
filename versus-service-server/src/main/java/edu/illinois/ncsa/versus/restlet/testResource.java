/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.Collection;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;



public class testResource extends ServerResource {

	@Get("html")
	public Representation get() {
			String content = new String("<h3>Test</h3><p>hello, world!</p>");
			Representation representation = new StringRepresentation(content,MediaType.TEXT_HTML);
			return representation;
		}
}
