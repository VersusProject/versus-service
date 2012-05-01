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

import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Multiple extractors.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class ExtractorsServerResource extends ServerResource {

	@Get
	public Representation asText() {
		Collection<Extractor> extractors = ((ServerApplication) getApplication())
				.getExtractors();
		if (extractors.size() == 0) {
			Representation representation = new StringRepresentation(
					"No extractors", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Extractors</h3>" + "<ul>");
			for (Extractor extractor : extractors) {
				content += "<li><a href='/versus/api/extractors/"
						+ extractor.getClass().getName() + "'>"
						+ extractor.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,
					MediaType.TEXT_HTML);
			return representation;
		}
	}
	
}
