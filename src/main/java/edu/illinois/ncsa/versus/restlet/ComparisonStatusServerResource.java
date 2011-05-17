/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 * @author lmarini
 * 
 */
public class ComparisonStatusServerResource extends ServerResource {

	@Get
	public Representation status() {
		String id = (String) getRequest().getAttributes().get("id");

		ComparisonServiceImpl comparisonService = ServerApplication
				.getInjector().getInstance(ComparisonServiceImpl.class);
		Comparison comparison = comparisonService.getComparison(id);
		ComparisonStatus status = comparison.getStatus();

		if (comparison.getValue() != null) {
			return new StringRepresentation(status.name(), MediaType.TEXT_HTML);
		} else {
			return new StringRepresentation("N/A", MediaType.TEXT_HTML);
		}
	}
}
