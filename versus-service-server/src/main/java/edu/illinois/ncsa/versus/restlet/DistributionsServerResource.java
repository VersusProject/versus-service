/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.restlet.Distribution.DistributionStatus;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonSubmitter;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.DistributionService;
import edu.illinois.ncsa.versus.store.DistributionServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

public class DistributionsServerResource extends VersusServerResource {
	
	
	@Get("html")
	public Representation list() {
			
		// Guice storage
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		DistributionService distributionService = injector.getInstance(DistributionServiceImpl.class);
		Collection<Distribution> distributions  = distributionService.listAll();

		if (distributions.size() == 0) {
			Representation representation = new StringRepresentation(
					"No distributions", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Distributions</h3>"
					+ "<ul>");
			for (Distribution distribution : distributions) {
				String id = distribution.getId();
				content += "<li><a href='/versus/api/distributions/" + id + "'>"
						+ id + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,
					MediaType.TEXT_HTML);
			return representation;
		}
	}
	
	@Post
	public Representation submit(Representation entity) {
					
		// Guice storage
		Injector injector                           = Guice.createInjector(new RepositoryModule());
		DistributionServiceImpl distributionService = injector.getInstance(DistributionServiceImpl.class);
		
		Distribution distribution = new Distribution();
		// parse entity
		Form form = new Form(entity);
		
		distribution.setId(UUID.randomUUID().toString());

		distribution.setDataset(form.getValuesArray("data")); //array of file urls
		distribution.setAdapterId(form.getFirstValue("adapter"));
		distribution.setExtractorId(form.getFirstValue("extractor"));
		distribution.setMeasureId(form.getFirstValue("measure"));
		
		distribution.setStatus(DistributionStatus.STARTED);
		distribution.setComparisonIds( submitComparisons(distribution) ); //submit and add comparison ids		
		distributionService.addDistribution(distribution);			

		return null;
	}
	
	private List<String> submitComparisons(Distribution distribution){
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);		
		List<String> fileUrls                   = new ArrayList<String>( Arrays.asList( distribution.getDataset() ) );
		String adapter                          = distribution.getAdapterId();
		String extractor                        = distribution.getExtractorId();
		String measure                          = distribution.getMeasureId();
		List<String> comparisonIds              = new ArrayList<String>();

		while( fileUrls.size() > 0){			
			for( int j=0; j<fileUrls.size(); j++){	
				
				String cid = comparisonService.findComparison(fileUrls.get(0), fileUrls.get(j), adapter, extractor, measure); //check if current comparison exists.
				
				if( cid == null ){			
					Comparison comparison = new Comparison(fileUrls.get(0), fileUrls.get(j), adapter, extractor, measure);
					String id             = comparison.getId();
                    try {
                        ComparisonSubmitter submitter = new ComparisonSubmitter((ServerApplication)getApplication(), comparison);
                        submitter.submit();
                    } catch (IOException ex) {
                        Logger.getLogger(DistributionsServerResource.class.getName()).log(Level.WARNING, null, ex);
                    } catch (NoSlaveAvailableException ex) {
                        Logger.getLogger(DistributionsServerResource.class.getName()).log(Level.WARNING, null, ex);
                    }
					comparisonIds.add(id);
				}
				else{
					comparisonIds.add(cid);
				}
			}			
			fileUrls.remove(0);
		}		

		return comparisonIds;
	}
	
}