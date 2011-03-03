package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Single comparison.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class ComparisonServerResource extends ServerResource {

	@Get
	public Comparison getBean() {
		String id = (String) getRequest().getAttributes().get("id");

		// Guice storage
		Injector injector = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		return comparisonService.getComparison(id);
	}

	@Get("xml")
	public Representation asXML() {
		String id = (String) getRequest().getAttributes().get("id");

		// Guice storage
		Injector injector = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		Comparison comparison = comparisonService.getComparison(id);

		if (comparison != null) {
			try {
				DomRepresentation representation = new DomRepresentation(
						MediaType.TEXT_XML);
				Document document = representation.getDocument();
				Element elementComparison = document
						.createElement("comparison");
				document.appendChild(elementComparison);
				// id
				Element elementId = document.createElement("id");
				elementId.appendChild(document.createTextNode(comparison
						.getId()));
				elementComparison.appendChild(elementId);
				// value
				Element valueId = document.createElement("value");
				if (comparison.getValue() != null) {
					valueId.appendChild(document.createTextNode(comparison
							.getValue().toString()));
				} else {
					valueId.appendChild(document.createTextNode("N/A"));
				}
				elementComparison.appendChild(valueId);
				// first dataset
				Element firstDataset = document.createElement("firstDataset");
				firstDataset.appendChild(document.createTextNode(comparison
						.getFirstDataset()));
				elementComparison.appendChild(firstDataset);
				// second dataset
				Element secondDataset = document.createElement("secondDataset");
				secondDataset.appendChild(document.createTextNode(comparison
						.getSecondDataset()));
				elementComparison.appendChild(secondDataset);
				// adapter
				Element adapter = document.createElement("adapter");
				adapter.appendChild(document.createTextNode(comparison
						.getAdapterId()));
				elementComparison.appendChild(adapter);
				// extractor
				Element extractor = document.createElement("extractor");
				extractor.appendChild(document.createTextNode(comparison
						.getExtractorId()));
				elementComparison.appendChild(extractor);
				// measure
				Element measure = document.createElement("measure");
				measure.appendChild(document.createTextNode(comparison
						.getMeasureId()));
				elementComparison.appendChild(measure);
				// normalize
				document.normalizeDocument();
				return representation;
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Error creating comparison xml",
						e);
				e.printStackTrace();
			}
		} else {
			return new StringRepresentation("A comparison with id " + id
					+ " does not exist.");
		}
		return new StringRepresentation("Error retrieving comparison with id "
				+ id);
	}
}
