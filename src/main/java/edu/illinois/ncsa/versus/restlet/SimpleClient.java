package edu.illinois.ncsa.versus.restlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import edu.illinois.ncsa.versus.restlet.comparison.Comparison;

/**
 * Simple client to versus web service.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class SimpleClient {

	private static Logger logger = Logger
			.getLogger("edu.illinois.ncsa.versus.restlet.SimpleClient");

	public static void main(String[] args) throws ResourceException,
			IOException {

		if (args.length != 1) {
			System.err
					.println("Please specify name of file that includes the request.");
		} else {

			Properties properties = new Properties();
			properties.load(new FileInputStream(args[0]));

			ClientResource comparisonsResource = new ClientResource(
					properties.getProperty("endpoint"));
			try {
				comparisonsResource.get().write(System.out);
				System.out.println("\n");
			} catch (ResourceException e) {
				System.out.println(e);
			}
			try {
				// first comparison
				Comparison comparison = new Comparison(
						properties.getProperty("dataset1"),
						properties.getProperty("dataset2"),
						properties.getProperty("adapter"),
						properties.getProperty("extractor"),
						properties.getProperty("measure"));
				Representation post = comparisonsResource
						.post(getComparisonRepresentation(comparison));
				post.write(System.out);
				System.out.println("\n");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ResourceException e) {
				logger.log(Level.SEVERE, "Error connecting to server", e);
			}
		}
	}

	private static Representation getComparisonRepresentation(
			Comparison comparison) {
		Form form = new Form();
		form.add("id", comparison.getId());
		form.add("dataset1", comparison.getFirstDataset());
		form.add("dataset2", comparison.getSecondDataset());
		form.add("adapter", comparison.getAdapterId());
		form.add("extractor", comparison.getExtractorId());
		form.add("measure", comparison.getMeasureId());
		return form.getWebRepresentation();
	}
}
