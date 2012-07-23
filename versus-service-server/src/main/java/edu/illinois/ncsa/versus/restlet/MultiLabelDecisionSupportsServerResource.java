/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport.MLDSInfo;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport.MLDS_Status;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonSubmitter;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonsServerResource;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.MultiLabelDecisionSupportServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

public class MultiLabelDecisionSupportsServerResource extends ServerResource {
	
	@Get("html")
	public Representation list() {
			
		// Guice storage
		Injector injector                                  = Guice.createInjector(new RepositoryModule());
		MultiLabelDecisionSupportServiceImpl dsService     = injector.getInstance(MultiLabelDecisionSupportServiceImpl.class);	
		Collection<MultiLabelDecisionSupport> dsCollection = dsService.listAll();

		if (dsCollection.size() == 0) {
			Representation representation = new StringRepresentation("No multi-label decision support trials", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Multi-Label Decision Support</h3>" + "<ul>");
			for (MultiLabelDecisionSupport ds : dsCollection) {
				String id = ds.getId();
				content += "<li><a href='/versus/api/multiLabelDecisionSupport/" + id + "'>" + id + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,MediaType.TEXT_HTML);
			return representation;
		}
	}
	
	@Post
	public Representation submit(Representation entity) {
					
		// Guice storage
		Injector injector                    		     = Guice.createInjector(new RepositoryModule());
		MultiLabelDecisionSupportServiceImpl mldsService = injector.getInstance(MultiLabelDecisionSupportServiceImpl.class);		
		MultiLabelDecisionSupport mlds                   = new MultiLabelDecisionSupport();
		// parse entity
		Form form = new Form(entity);
		
		mlds.setId(UUID.randomUUID().toString());
		//get the number of "clusters" specified
		
		int k = Integer.parseInt(form.getFirstValue("k"));
		mlds.setK(k);
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		for( int i=0; i<k; i++){
			ArrayList<String> l = new ArrayList<String>( Arrays.asList( form.getValuesArray("data"+i) ));
			data.add(l);
		}
		
		mlds.setMethod(form.getFirstValue("method"));
		
		mlds.setData( data ); //array of similar file urls
		
		mlds.setAdapterId(form.getFirstValue("adapter"));		
		mlds.setAvailableExtractors( getExtractors() );
		mlds.setAvailableMeasures( getMeasures() );
		mlds.getSupportedMethods(); //set all of the mlds data here, by checking all of the extractors and measures
		mlds.setStatus(MLDS_Status.STARTED);
		submitComparisons(mlds);
		//add the service;
		mldsService.addMultiLabelDecisionSupport( mlds );	
		//run the service
		mlds.getBestPair();
		return null;
	}
	
	private void submitComparisons(MultiLabelDecisionSupport mlds){
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);		
		ArrayList<ArrayList<String>> data       = mlds.getData();
		ArrayList<MLDSInfo> decisionSupportData = mlds.getMultiLabelDecisionSupportData(); 
		ComparisonsServerResource a             = new ComparisonsServerResource(); 

		for( int x=0; x<decisionSupportData.size(); x++){ //num of <extractor, measure>
			
			ArrayList<ArrayList<String>> comparisonIds  = new ArrayList<ArrayList<String>>();
			
			for( int i=0; i<mlds.getK(); i++){ // num of k classes
				
				ArrayList<String> kIds  = new ArrayList<String>();				
				ArrayList<String> files = new ArrayList<String>(data.get(i));
				
				//setup file comparisons
				while( !files.isEmpty() ){
					
					for( int j=0; j<files.size(); j++){	
						
						String cid = comparisonService.findComparison(files.get(0), files.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID()); //check if current comparison exists.
						
						if( cid == null ){			
                            String adapter = decisionSupportData.get(x).getAdapterID();
                            String extractor = decisionSupportData.get(x).getExtractorID();
                            String measure = decisionSupportData.get(x).getMeasureID();
                            String dataset1Name = files.get(0);
                            String dataset2Name =  files.get(j);
                            InputStream dataset1Stream;
                            InputStream dataset2Stream;
							Comparison comparison = new Comparison(dataset1Name, dataset2Name, adapter, extractor, measure);
							String id = comparison.getId();
                            ServerApplication server = (ServerApplication) getApplication();
                            if (server.getRegistry().supportComparison(adapter, extractor, measure)) {
								try {
                                    dataset1Stream = new URL(dataset1Name).openStream();
                                    dataset2Stream = new URL(dataset2Name).openStream();
                                    ComparisonSubmitter submitter = new ComparisonSubmitter(server, comparison, dataset1Stream, dataset2Stream);
                                    submitter.submit();
								} catch (IOException e) {
									getLogger().log(Level.SEVERE, "Error creating local job", e);
								}catch (NoSlaveAvailableException e) {
									getLogger().log(Level.SEVERE, "Error creating local job", e);
								}
                            }
							kIds.add(id);
						}
						else{
							kIds.add(cid);
						}
					}			
					files.remove(0);
				}// end while
				comparisonIds.add(kIds);
			}// end for k
			decisionSupportData.get(x).setComparisons(comparisonIds);
		} //end for E,M
	}
	
	private List<Extractor> getExtractors(){
		
		List<Extractor> eNames           = new ArrayList<Extractor>();		
		Collection<Extractor> extractors = ((ServerApplication) getApplication()).
                getRegistry().getAvailableExtractors();
		for (Extractor extractor : extractors) {
			eNames.add(extractor);
		}
		return eNames;
	}
	
	private List<Measure> getMeasures(){
		
		List<Measure> mNames         = new ArrayList<Measure>();
		Collection<Measure> measures = ((ServerApplication) getApplication()).
                getRegistry().getAvailableMeasures();	
		
		for (Measure measure : measures) {
			mNames.add(measure);
		}
		return mNames;
	}
}