package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import java.lang.String;

/**
 * Simple testing client.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class simpleDistributionTest {

	private static Logger logger = Logger
			.getLogger("edu.illinois.ncsa.versus.restlet.simpleDistributionTest");

	public static void main(String[] args) throws ResourceException,
			IOException {
		// test comparisons resource
		ClientResource distributionsResource = new ClientResource(
				"http://localhost:8182/versus/api/distributions");
		try {
			distributionsResource.get().write(System.out);
			System.out.println("\n");
		} catch (ResourceException e) {
			System.out.println(e);
		}
		try {

			String[] urlList = new String[4];
			urlList[0]       = "http://isda.ncsa.illinois.edu/img/ISDA-logo.png";
			urlList[1]       = "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif";
			urlList[2]       = "http://isda.ncsa.illinois.edu/drupal/sites/default/files/newsflash_logo.jpg";
			urlList[3]       = "http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/eScience2010_0.jpg";
			
			Distribution distribution = new Distribution(
					urlList,
					"edu.illinois.ncsa.versus.adapter.impl.BytesAdapter",
					"edu.illinois.ncsa.versus.extract.impl.MD5Extractor",
					"edu.illinois.ncsa.versus.measure.impl.MD5DistanceMeasure");
			distributionsResource.post(getDistributionRepresentation(distribution));
			
		} catch (ResourceException e) {
			logger.log(Level.SEVERE, "Error connecting to server", e);
		}

	}

	private static Representation getDistributionRepresentation(Distribution distribution) {
		Form form = new Form();
		
		String[] dataSet = distribution.getDataset();
		for( int i=0; i<dataSet.length; i++){
			form.add("data", dataSet[i] );		
		}
		form.add("adapter", distribution.getAdapterId());
		form.add("extractor", distribution.getExtractorId());
		form.add("measure", distribution.getMeasureId());
		return form.getWebRepresentation();
	}
}
