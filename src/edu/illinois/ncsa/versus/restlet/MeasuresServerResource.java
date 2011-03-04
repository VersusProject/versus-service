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

import edu.illinois.ncsa.versus.measure.Measure;

/**
 * Multiple measures.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class MeasuresServerResource extends ServerResource {

	@Get
	public Representation asText() {
		Collection<Measure> measures = ((ServerApplication) getApplication())
				.getMeasures();
		if (measures.size() == 0) {
			Representation representation = new StringRepresentation(
					"No measures", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Mesures</h3>" + "<ul>");
			for (Measure measure : measures) {
				content += "<li><a href='/versus/api/measures/"
						+ measure.getClass().getName() + "'>"
						+ measure.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,
					MediaType.TEXT_HTML);
			return representation;
		}
	}
}
