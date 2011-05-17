package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

/**
 * Simple testing client.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class SimpleClientTest {

	private static Logger logger = Logger
			.getLogger("edu.illinois.ncsa.versus.restlet.SimpleClientTest");

	public static void main(String[] args) throws ResourceException,
			IOException {
		// test comparisons resource
		ClientResource comparisonsResource = new ClientResource(
				"http://localhost:8182/versus/api/comparisons");
		try {
			comparisonsResource.get().write(System.out);
			System.out.println("\n");
		} catch (ResourceException e) {
			System.out.println(e);
		}
		try {
			// first comparison
			Comparison comparison = new Comparison(
					"",
					"http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
					"http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
					"edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter",
					"edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor",
					"edu.illinois.ncsa.versus.measure.impl.HistogramDistanceMeasure");
			comparisonsResource.post(getComparisonRepresentation(comparison));
			// second comparison
			Comparison comparison2 = new Comparison("",
					"http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
					"http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
					"edu.illinois.ncsa.versus.adapter.impl.BytesAdapter",
					"edu.illinois.ncsa.versus.extract.impl.MD5Extractor",
					"edu.illinois.ncsa.versus.measure.impl.MD5DistanceMeasure");
			comparisonsResource.post(getComparisonRepresentation(comparison2));
		} catch (ResourceException e) {
			logger.log(Level.SEVERE, "Error connecting to server", e);
		}
		// test measures resource
		String nonExistentMeasure = "http://localhost:8182/versus/measures/blah";
		ClientResource measuresResource = new ClientResource(nonExistentMeasure);
		try {
			Representation representation = measuresResource.get();
			representation.write(System.out);
		} catch (ResourceException e) {
			logger.log(Level.SEVERE, nonExistentMeasure + " not found \n", e);
		}
		// // test json serialization
		// ClientResource firstComparisonResource = new ClientResource(
		// "http://localhost:8182/versus/comparisons");
		// firstComparisonResource.wrap(ComparisonServerResource.class);
		// firstComparisonResource.get(MediaType.APPLICATION_JSON).write(
		// System.out);

		// first comparison
		Comparison comparison3 = new Comparison("",
				"http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
				"http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
				"edu.illinois.ncsa.versus.adapter.impl.DummyAdapter",
				"edu.illinois.ncsa.versus.extract.impl.DummyExtractor",
				"edu.illinois.ncsa.versus.measure.impl.DummyMeasure");
		comparisonsResource.post(getComparisonRepresentation(comparison3));
	}

	private static Representation getComparisonRepresentation(
			Comparison comparison) {
		Form form = new Form();
		form.add("dataset1", comparison.getFirstDataset());
		form.add("dataset2", comparison.getSecondDataset());
		form.add("adapter", comparison.getAdapterId());
		form.add("extractor", comparison.getExtractorId());
		form.add("measure", comparison.getMeasureId());
		return form.getWebRepresentation();
	}
}
