/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.Collection;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.adapter.Adapter;

/**
 * Multiple adapters.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class AdaptersServerResource extends ServerResource {

	@Get
	public Representation asText() {
		Collection<Adapter> adapters = ((ServerApplication) getApplication())
				.getAdapters();
		if (adapters.size() == 0) {
			Representation representation = new StringRepresentation(
					"No adapters", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Adapters</h3>" + "<ul>");
			for (Adapter adapter : adapters) {
				content += "<li><a href='/versus/adapters/"
						+ adapter.getClass().getName() + "'>"
						+ adapter.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,
					MediaType.TEXT_HTML);
			return representation;
		}
	}
}
