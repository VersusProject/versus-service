/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * @author lmarini
 * 
 */
public class ComparisonStatusServerResource extends ServerResource {

	@Get
	public Representation status() {
		String id = (String) getRequest().getAttributes().get("id");

		Injector injector = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		Comparison comparison = comparisonService.getComparison(id);

		if (comparison.getValue() != null) {
			return new StringRepresentation(String.valueOf(comparison
					.getValue()), MediaType.TEXT_HTML);
		} else {
			return new StringRepresentation("N/A", MediaType.TEXT_HTML);
		}
	}
}
