package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.ArrayList;
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
public class MultiLabelDecisionSupportTest2 {

	private static Logger logger = Logger
			.getLogger("edu.illinois.ncsa.versus.restlet.multiLabelDecisionSupportTest");

	public static void main(String[] args) throws ResourceException,
			IOException {
		// test comparisons resource
		ClientResource dsResource = new ClientResource(
				"http://localhost:8182/versus/api/multiLabelDecisionSupport");
		try {
			dsResource.get().write(System.out);
			System.out.println("\n");
		} catch (ResourceException e) {
			System.out.println(e);
		}
		try {
			
			ArrayList<String> s1 = new ArrayList<String>();
			ArrayList<String> s2 = new ArrayList<String>();
			ArrayList<String> s3 = new ArrayList<String>();
			
			s1.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/eservices_presentation.preview.jpg");
			s1.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/CI_fig51_sm.jpg");
			
//			s1.add("http://www.glerl.noaa.gov/webcams/images/lmfs1.jpg");
//			s1.add("http://www.glerl.noaa.gov/webcams/images/lmfs2.jpg");
			
			s2.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/newsflash_logo.jpg");
			s2.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/images/eScience2010_0.jpg");
			
//			s2.add("http://upload.wikimedia.org/wikipedia/commons/5/54/DLF_IT_Park_-_Rajarhat_2012-04-11_9380.JPG");
//			s2.add("http://upload.wikimedia.org/wikipedia/commons/3/33/Uniworld_City_entrance.jpg");
			
			s3.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/pictures/picture-5.jpg");
			s3.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/pictures/picture-18.jpg");
			s3.add("http://isda.ncsa.illinois.edu/drupal/sites/default/files/pictures/picture-34.jpg");
			
			ArrayList<ArrayList<String>> d = new ArrayList<ArrayList<String>>();
			d.add(s1);
			d.add(s2);
			d.add(s3);

			int k = 3;
			
			MultiLabelDecisionSupport mlds = new MultiLabelDecisionSupport(d, "edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter",k,"probabilistic");
			
			dsResource.post(getMultiLabelDecisionSupportRepresentation(mlds));
			
		} catch (ResourceException e) {
			logger.log(Level.SEVERE, "Error connecting to server", e);
		}

	}

	private static Representation getMultiLabelDecisionSupportRepresentation(MultiLabelDecisionSupport ds) {
		
		Form form = new Form();
		
		ArrayList<ArrayList<String>> sData = ds.getData();
		
		for( int i=0; i<sData.size(); i++){
			for( int j=0; j<sData.get(i).size(); j++){
				form.add("data"+i, sData.get(i).get(j) );
			}
		}
		
		form.add("k", Integer.toString(ds.getK()) );		
		form.add("adapter", ds.getAdapterId());
		form.add("method",ds.getMethod());
		return form.getWebRepresentation();
	}
}
