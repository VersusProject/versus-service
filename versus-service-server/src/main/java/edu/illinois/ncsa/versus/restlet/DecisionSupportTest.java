package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.List;
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
 * @author Devin Bonnie
 * 
 */
public class DecisionSupportTest {

	private static Logger logger = Logger
			.getLogger("edu.illinois.ncsa.versus.restlet.decisionSupportTest");

	public static void main(String[] args) throws ResourceException,
			IOException {
		// test comparisons resource
		ClientResource dsResource = new ClientResource(
				"http://localhost:8182/versus/api/decisionSupport");
		try {
			dsResource.get().write(System.out);
			System.out.println("\n");
		} catch (ResourceException e) {
			System.out.println(e);
		}
		try {

			String[] sList = {"http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/eservices_presentation.preview.jpg","http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/CI_fig51_sm.jpg"};
			String[] dList = {"http://isda.ncsa.illinois.edu/drupal/sites/default/files/newsflash_logo.jpg","http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/eScience2010_0.jpg","http://isda.ncsa.illinois.edu/drupal/sites/default/files/pictures/picture-5.jpg"};
			
			DecisionSupport ds = new DecisionSupport(
					sList,
					dList,
					"edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter"
					);
			dsResource.post(getDecisionSupportRepresentation(ds));
			
		} catch (ResourceException e) {
			logger.log(Level.SEVERE, "Error connecting to server", e);
		}

	}

	private static Representation getDecisionSupportRepresentation(DecisionSupport ds) {
		Form form = new Form();
		
		List<String> sData = ds.getSimilarData();
		for( int i=0; i<sData.size(); i++){
			form.add("similarData", sData.get(i) );		
		}
		
		List<String> dData = ds.getDissimilarData();
		for( int i=0; i<dData.size(); i++){
			form.add("dissimilarData", dData.get(i) );		
		}
		
		form.add("adapter", ds.getAdapterId());
		return form.getWebRepresentation();
	}
}
