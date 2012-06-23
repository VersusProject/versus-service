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

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.restlet.DecisionSupport.DSInfo;
import edu.illinois.ncsa.versus.restlet.DecisionSupport.DS_Status;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.DecisionSupportServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

public class DecisionSupportsServerResource extends ServerResource {
	
	
	@Get("html")
	public Representation list() {
			
		// Guice storage
		Injector injector                        = Guice.createInjector(new RepositoryModule());
		DecisionSupportServiceImpl dsService     = injector.getInstance(DecisionSupportServiceImpl.class);	
		Collection<DecisionSupport> dsCollection = dsService.listAll();

		if (dsCollection.size() == 0) {
			Representation representation = new StringRepresentation("No decision support trials", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Decision Support</h3>" + "<ul>");
			for (DecisionSupport ds : dsCollection) {
				String id = ds.getId();
				content += "<li><a href='/versus/api/decisionSupport/" + id + "'>" + id + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,MediaType.TEXT_HTML);
			return representation;
		}
	}
	
	@Post
	public Representation submit(Representation entity) {
					
		// Guice storage
		Injector injector                    = Guice.createInjector(new RepositoryModule());
		DecisionSupportServiceImpl dsService = injector.getInstance(DecisionSupportServiceImpl.class);		
		DecisionSupport ds                   = new DecisionSupport();
		// parse entity
		Form form = new Form(entity);
		
		ds.setId(UUID.randomUUID().toString());
		ds.setSimilarData( Arrays.asList( form.getValuesArray("similarData") ) ); //array of similar file urls
		ds.setDissimilarData( Arrays.asList( form.getValuesArray("dissimilarData") ) ); //array of dissimilar file urls
		ds.setAdapterId(form.getFirstValue("adapter"));		
		
		ds.setAvailableExtractors( getExtractors() );
		ds.setAvailableMeasures( getMeasures() );
		ds.getSupportedMethods();
		ds.setStatus(DS_Status.STARTED);
		submitSimilarComparisons(ds);
		//add the service;
		dsService.addDecisionSupport( ds );	
		//run the service
		ds.getBestPair();
		return null;
	}
	
	private void submitSimilarComparisons(DecisionSupport ds){
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);		
		ArrayList<String> similarFiles          = new ArrayList<String>( ds.getSimilarData() );
		ArrayList<String> dissimilarFiles       = new ArrayList<String>(ds.getDissimilarData() );		
		ArrayList<DSInfo> decisionSupportData   = ds.getDecisionSupportData(); 
		
		ArrayList<String> s_comparisonIds       = new ArrayList<String>();
		ArrayList<String> d_comparisonIds       = new ArrayList<String>();
		ComparisonsServerResource a             = new ComparisonsServerResource(); 

		for( int x=0; x<decisionSupportData.size(); x++){
			
			//setup similar file comparisons
			while( !similarFiles.isEmpty() ){
				
				for( int j=0; j<similarFiles.size(); j++){	
					
					String cid = comparisonService.findComparison(similarFiles.get(0), similarFiles.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID()); //check if current comparison exists.
					
					if( cid == null ){
						String id             = UUID.randomUUID().toString();			
						Comparison comparison = new Comparison(id, similarFiles.get(0), similarFiles.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID());;
											
						if (a.checkRequirements(comparison)) {
							try {
								a.createJob(comparison);
							} catch (IOException e) {
								getLogger().log(Level.SEVERE, "Error creating local job", e);
							}
						} 
						s_comparisonIds.add(id);
					}
					else{
						s_comparisonIds.add(cid);
					}
				}			
				similarFiles.remove(0);
			}
			//setup dissimilar file comparisons
			while( !dissimilarFiles.isEmpty() ){			
				for( int j=0; j<dissimilarFiles.size(); j++){	
					
					String cid = comparisonService.findComparison(dissimilarFiles.get(0), dissimilarFiles.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID()); //check if current comparison exists.
					
					if( cid == null ){
						String id             = UUID.randomUUID().toString();			
						Comparison comparison = new Comparison(id, dissimilarFiles.get(0), dissimilarFiles.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID());
											
						if (a.checkRequirements(comparison)) {
							try {
								a.createJob(comparison);
							} catch (IOException e) {
								getLogger().log(Level.SEVERE, "Error creating local job", e);
							}
						} 
						d_comparisonIds.add(id);
					}
					else{
						d_comparisonIds.add(cid);
					}
				}			
				dissimilarFiles.remove(0);
			}
			similarFiles    = new ArrayList<String>( ds.getSimilarData() );
			dissimilarFiles = new ArrayList<String>( ds.getDissimilarData() );
			//add similarFiles and dissimilarFiles to the corresponding DSInfo
			decisionSupportData.get(x).setSimilarComparisons(s_comparisonIds);
			decisionSupportData.get(x).setDissimilarComparisons(d_comparisonIds);
		}
		ds.setDecisionSupportData(decisionSupportData);
	}
	
	private List<Extractor> getExtractors(){
		
		List<Extractor> eNames           = new ArrayList<Extractor>();		
		Collection<Extractor> extractors = ((ServerApplication) getApplication()).getExtractors();
		for (Extractor extractor : extractors) {
			eNames.add(extractor);
		}
		return eNames;
	}
	
	private List<Measure> getMeasures(){
		
		List<Measure> mNames         = new ArrayList<Measure>();
		Collection<Measure> measures = (new ServerApplication()).getMeasures();	
		
		for (Measure measure : measures) {
			mNames.add(measure);
		}
		return mNames;
	}
}