package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.Comparison;
import edu.illinois.ncsa.versus.restlet.ComparisonsServerResource;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport;
import edu.illinois.ncsa.versus.restlet.DecisionSupport.DS_Status;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport.MLDSInfo;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport.MLDS_Status;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.MultiLabelDecisionSupportServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;
import edu.illinois.ncsa.versus.rest.ComparisonResource;

@Path("/multiLabelDecisionSupport")
public class MultiLabelDecisionSupportResource {

	private static Log log = LogFactory.getLog(MultiLabelDecisionSupportResource.class);
	
	@GET
	@Produces("text/html")
	public String list(@Context ServletContext context){
		
		Injector injector                                  = Guice.createInjector(new RepositoryModule());
		MultiLabelDecisionSupportServiceImpl dsService     = injector.getInstance(MultiLabelDecisionSupportServiceImpl.class);	
		Collection<MultiLabelDecisionSupport> dsCollection = dsService.listAll();

		if (dsCollection.size() == 0) {
			return "No multi-label decision support trials";
					
		} else {
			String content = new String("<h3>Versus > Multi-Label Decision Support</h3>" + "<ul>");
			for (MultiLabelDecisionSupport ds : dsCollection) {
				String id = ds.getId();
				content += "<li><a href='/api/v1/multiLabelDecisionSupport/" + id + "'>" + id + "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}
	
	@POST
	@Produces("text/html")
	//@Consumes("application_form_urlencoded")
	@Consumes("application/x-www-form-urlencoded")
	//public String submit(@Form MLDSForm form, @Context ServletContext context) throws IOException{
	public String submit(MultivaluedMap<String, String> form, @Context ServletContext context) throws IOException{
		// Guice storage
				Injector injector                    		     = Guice.createInjector(new RepositoryModule());
				MultiLabelDecisionSupportServiceImpl mldsService = injector.getInstance(MultiLabelDecisionSupportServiceImpl.class);		
				CompareRegistry registry = (CompareRegistry) context
						.getAttribute(CompareRegistry.class.getName());
				ExecutionEngine engine = (ExecutionEngine) context
						.getAttribute(ExecutionEngine.class.getName());
				
				
				MultiLabelDecisionSupport mlds                   = new MultiLabelDecisionSupport();
		
				mlds.setId(UUID.randomUUID().toString());
				//get the number of "clusters" specified
				/*log.debug("K= "+form.k);
				log.debug("adapter="+form.adapter);
				log.debug("method="+form.method);*/
				//log.debug("data="+form.data);
				
			//	int k = Integer.parseInt(form.k);
				int k=Integer.parseInt(form.getFirst("k"));
				log.debug("K= "+k);
				
				mlds.setK(k);
				
				ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
				
				//MultivaluedMap<String, String> mlfiles=form.asMap(); 
				
				int m=0,n=0;
				
				for( int i=0; i<k; i++){
					//ArrayList<String> l = new ArrayList<String>(mlfiles.get(Integer.toString(i)));
					//ArrayList<String>l=new ArrayList<String>(Arrays.asList(form.data.get(Integer.toString(i))));
					//ArrayList<String>l=new ArrayList<String>();
					//l=(ArrayList<String>) form.data.get(i);
					//Collection<String>l=new Collection<String>();
					//mlfiles.put(Integer.toString(i),Form.asMap(form.data));
					//l=form.data.;
					
					/*for(m=n;m<form.data.size();m++){
						if(form.data.get(m).label.equals(Integer.toString(i))){
					      l.add(form.data.get(i).url);
					       n=m;
						}
					}*/
					
					//l.add(form.data.get(i+0));
					//l.add(form.data.get(i+1));
					data.add((ArrayList<String>) form.get("data"+i));
					
					//l.add(form.data.get(i).get(i+0));
					//l.add(form.data.get(i).get(i+1));
					//log.debug("k="+k+"array="+l);
					//data.add(l);
				}
				for(int i=0;i<data.size();i++){
					for(int j=0;j<data.get(i).size();j++){
						log.debug("data.size()="+data.size()+" i="+i+"data.get(i).size()="+data.get(i).size()+" j="+j+" data="+ data.get(i).get(j));
					}
				}
				
				//mlds.setMethod(form.method);
				mlds.setMethod(form.getFirst("method"));
				mlds.setData( data ); //array of similar file urls
				
				//mlds.setAdapterId(form.adapter);	
				mlds.setAdapterId(form.getFirst("adapter"));
				mlds.setAvailableExtractors( new ArrayList<Extractor>(getExtractors(context)));
				mlds.setAvailableMeasures( new ArrayList<Measure>(getMeasures(context)));
				mlds.getSupportedMethods(); //set all of the mlds data here, by checking all of the extractors and measures
				mlds.setStatus(MLDS_Status.STARTED);
				submitComparisons(mlds,registry,engine);
				//add the service;
				mldsService.addMultiLabelDecisionSupport( mlds );	
				//run the service
				mlds.getBestPair();
				return mlds.getId();
				//return null;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Map<String,Object>getJson(@PathParam("id")String id,@Context ServletContext context){
		Injector injector=(Injector) context.getAttribute(Injector.class.getName());
		MultiLabelDecisionSupportServiceImpl mldsService=injector.getInstance(MultiLabelDecisionSupportServiceImpl.class);
		MultiLabelDecisionSupport mlds=mldsService.getMultiLabelDecisionSupport(id);
		log.debug("GET:Entered getjson multilabel decision support");
		while(mlds.getStatus()!=MLDS_Status.DONE){}
		/*if(mlds.getStatus()!=MLDS_Status.DONE){
			if(mlds.getStatus()!=MLDS_Status.RUNNING || mlds.getStatus()!=MLDS_Status.DONE){
				mlds.getBestPair();
			}
		}*/
		Map<String,Object> json=new HashMap<String,Object>();
		json.put("id", mlds.getId());
		json.put("status", mlds.getStatus());
		json.put("rankedResults", mlds.getBestResultsList());
		return json;
		
	}
private void submitComparisons(MultiLabelDecisionSupport mlds,CompareRegistry registry, ExecutionEngine engine)
		throws IOException {
		
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);		
		
		//ArrayList<ArrayList<String>> data       = new ArrayList<ArrayList<String>>();
		//		data=mlds.getData();
		
		ArrayList<ArrayList<String>> comparisonIds;
		ArrayList<String> kIds;
		ArrayList<String> files;
		
		ArrayList<MLDSInfo> decisionSupportData = mlds.getMultiLabelDecisionSupportData(); 
		
		ComparisonResource a             = new ComparisonResource(); 

		log.debug("mlds.getData()="+mlds.getData().size()+"decisionSupportData.size()="+decisionSupportData.size());
		
		
		
		for( int x=0; x<decisionSupportData.size(); x++){ //num of <extractor, measure>
			
			       comparisonIds  = new ArrayList<ArrayList<String>>();
			       
			       log.debug("x="+ x+" mlds.getK()="+mlds.getK());
			       
			for( int i=0; i<mlds.getK(); i++){ // num of k classes
				 
				log.debug("x="+ x+"i="+i+" mlds.getK()="+mlds.getK()+" mlds.getData().get(i).size()="+mlds.getData().get(i).size());
				
				  kIds= new ArrayList<String>();		// kIds:comparison ids for each label	
				//ArrayList<String> files = new ArrayList<String>(data.get(i));      //checking the ArrayList assignments
				 files=new ArrayList<String>(mlds.getData().get(i));
				//files=data.get(i);                                      // assigning the files for each label
				 //log.debug("x="+ x+" i="+i+" mlds.getK()="+mlds.getK()+" files.size()="+files.size());
				
				//setup file comparisons
				while( !files.isEmpty() ){
					
					for( int j=1; j<files.size(); j++){	
						
						String cid = comparisonService.findComparison(files.get(0), files.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID()); //check if current comparison exists.
						
						if( cid == null ){
							String id             = UUID.randomUUID().toString();			
							Comparison comparison = new Comparison(id, files.get(0), files.get(j), decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID());
							
							if (a.checkRequirements(registry, decisionSupportData.get(x).getAdapterID(), decisionSupportData.get(x).getExtractorID(), decisionSupportData.get(x).getMeasureID())){
								
								PairwiseComparison pairwiseComparison = new PairwiseComparison();
								pairwiseComparison.setId(comparison.getId());
								pairwiseComparison.setFirstDataset(a
										.getFile(comparison.getFirstDataset()));
								pairwiseComparison.setSecondDataset(a
										.getFile(comparison.getSecondDataset()));
								pairwiseComparison.setAdapterId(comparison
										.getAdapterId());
								pairwiseComparison.setExtractorId(comparison
										.getExtractorId());
								pairwiseComparison.setMeasureId(comparison
										.getMeasureId());
								
							//if (a.checkRequirements(comparison)) {
								try {
									a.createLocalJob(pairwiseComparison,files.get(0),files.get(j), comparisonService,engine);
															
								} catch (IOException e) {
									log.error("Error creating local job", e);
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
				log.debug("i="+i+" kIds.size()="+kIds.size());
				comparisonIds.add(kIds);
			}// end for k
			log.debug("x="+x+"comparisonIds.size()="+comparisonIds.size());
			decisionSupportData.get(x).setComparisons(comparisonIds);
		} //end for E,M
	}
	
	
	private Collection<Extractor> getExtractors(ServletContext context) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		return registry.getAvailableExtractors();
	}

	private Collection<Measure> getMeasures(ServletContext context) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		return registry.getAvailableMeasures();
	}
	public static class MLDSForm{
		@FormParam("k")
		private String k;
		
		@FormParam("method")
		private String method;
		
		@FormParam("adapter")
		private String adapter;
		
		//@FormParam("data"+k)
		//private MultivaluedMap<String,String> data;
		//private ArrayList<ArrayList<String>> data;
		
		//@Form(prefix="data")
		@FormParam("data")
		private List<String> data;
		//private List<DATA> data;
		//@Override
		public String toString(){
			return "k="+k+"&method="+method+"&adapter="+adapter+"&data="+data;
		}
		
	}
	/*public static class DATA{
		@FormParam("label") private String label;
		@FormParam("url") private String url;
		//public DATA(){
			
		//}
		@Override
		public String toString(){
			return "label="+label+"&url="+url;
			
		}
	}*/
	
}
