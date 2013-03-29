package edu.illinois.ncsa.versus.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.Form;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.engine.impl.IndexingEngine;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.rest.DecisionSupportResource.DSForm;
import edu.illinois.ncsa.versus.restlet.DecisionSupport;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;
import edu.illinois.ncsa.versus.store.DecisionSupportServiceImpl;
import edu.illinois.ncsa.versus.store.IndexServiceImpl;
import edu.illinois.ncsa.versus.store.IndexerServiceImpl;

@Path("/index")
public class IndexResource {
	private static Log log = LogFactory.getLog(IndexResource.class);

	@GET
	@Produces("text/html")
	public String list(@Context ServletContext context) {

		// Guice storage
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);

		Collection<Index> indexCollection = indexService.listAll();

		if (indexCollection.size() == 0) {
			return "No Index running";
		} else {
			String content = new String("<h3>Versus > Index Collection</h3>"
					+ "<ul>");
			for (Index in : indexCollection) {
				String id = in.getId();
				content += "<li><a href='/api/v1/index/" + id + "'>" + id
						+ "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String submit(@Form IndexForm form, @Context ServletContext context)
			throws IOException {

		IndexingEngine inengine = (IndexingEngine) context
				.getAttribute(IndexingEngine.class.getName());

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);

		IndexerServiceImpl indexerService = injector
				.getInstance(IndexerServiceImpl.class);

		// Indexer indexerService=injector.getInstance(LinearIndexer.class);

		Index index = new Index();
		Indexer indexer;

		index.setId(UUID.randomUUID().toString());

		index.setAdapterId(form.adapter);
		index.setExtractorId(form.extractor);
		index.setMeasureId(form.measure);
		index.setIndexerId(form.indexer);

		/*
		 * index.setAdapter(form.adapter); index.setExtractor(form.extractor);
		 * index.setMeasure(form.measure); //index.setIndexer(form.indexer);
		 * index.setIndexer(indexerService);
		 */

		// indexer=inengine.createIndexerInstance(form.indexer);
		// indexer=new LinearIndexer();
		/*
		 * if(form.indexer.contains("LinearIndexer")) { indexer=new
		 * LinearIndexer(indexerService.getIndexerProcessor()); } else{
		 * indexer=new CensusIndexer(); } indexer.setId(index.getId());
		 * indexerService.addIndexer(indexer);
		 */
		indexService.addIndex(index);

		return index.getId();

	}

	@GET
	@Path("/{id}")
	@Produces("text/html")
	public String listindex(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);

		IndexerServiceImpl indexerService = injector
				.getInstance(IndexerServiceImpl.class);

		Index index = indexService.getIndex(id);
		String indexerId = index.getIndexerId();

		Indexer indexer;
		// indexerService.getIndexer(id);

		if (indexerService.existIndexer(index.getId())) {
			indexer = indexerService.getIndexer(index.getId());
		} else {
			if (indexerId.contains("LinearIndexerMem")) {
				//indexer = new LinearIndexer(
				//		indexerService.getIndexerProcessor());
				indexer = new LinearIndexerMem();
			} else if(indexerId.contains("LinearIndexerDisk")){
				indexer=new LinearIndexerDisk();
			}
			else {
				indexer = new CensusIndexerMem();
			}
			
			indexer.setId(index.getId());
			indexerService.addIndexer(indexer);
		}

		String content = new String("<h3>Versus > Index Instance</h3>" + "<ul>");
		content += "<li>" + index.getAdapterId() + "</li>";
		content += "<li>" + index.getExtractorId() + "</li>";
		content += "<li>" + index.getMeasureId() + "</li>";
		content += "<li>" + index.getIndexerId() + "</li>";

		/* search for the indexer based on id and list all file ids stored on it */
		if(indexer.getIdentifiers()==null){
		//if (indexer.getIdentifiers().size() == 0) {
			content += "<li>No Files in the index</li>";
			content += "</ul>";
			return content;
		} else {

			content += "<li> List of File Ids</li>";
			// for (String id1 : index.getindexHash().keySet()) {
			for (String id1 : indexer.getIdentifiers()) {

				content += "<li>" + id1 + "</li>";
			}

		}
		content += "</ul>";
		return content;
	}

	@POST
	@Path("/{id}/add")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addToindex(@Form UploadForm form, @PathParam("id") String id,
			@Context ServletContext context) {

		IndexingEngine inengine = (IndexingEngine) context
				.getAttribute(IndexingEngine.class.getName());
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);

		IndexerServiceImpl indexerService = injector
				.getInstance(IndexerServiceImpl.class);

		String fileurl = form.infile;
		File inputFile = null;
		/*
		 * Adapter adapter=null; Extractor extractor=null; Indexer indexer=null;
		 */

		Index index = indexService.getIndex(id);
		String indexerId=index.getIndexerId();
		Indexer indexer;
		//= indexerService.getIndexer(id);
		
		if (indexerService.existIndexer(index.getId())) {
			indexer = indexerService.getIndexer(index.getId());
		} else {
			if (indexerId.contains("LinearIndexerMem")) {
				//indexer = new LinearIndexer(
				//		indexerService.getIndexerProcessor());
				indexer = new LinearIndexerMem();
			} else if(indexerId.contains("LinearIndexerDisk")){
				indexer=new LinearIndexerDisk();
			}
			else {
				indexer = new CensusIndexerMem();
			}
			
			indexer.setId(index.getId());
			indexerService.addIndexer(indexer);
		}
		
		/*
		 * adapter=index.getAdapter(); extractor=index.getExtractor();
		 * indexer=index.getIndexer();
		 */

		String adapter = index.getAdapterId();
		String extractor = index.getExtractorId();
		// String indexer=index.getIndexerId();
		// indexer=indexService.

		try {
			inputFile = ComparisonResource.getFile(fileurl);
			// inputFile.getName()
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (inputFile.isFile()) {
			log.debug("isFile");

			try {
				inengine.addDocument(inputFile, adapter, extractor, indexer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return "Done";

	}

	
	  @GET
	  @Path("/{id}/build")
	  @Produces("text/html") 
	  public String buildindex(@PathParam("id") String id,@Context ServletContext context) {
	  
	  Injector injector = (Injector) context.getAttribute(Injector.class.getName()); 
	  IndexServiceImpl indexService = injector.getInstance(IndexServiceImpl.class);
	  IndexerServiceImpl indexerService=injector.getInstance(IndexerServiceImpl.class);
	  
	  Index index = indexService.getIndex(id);
	  Indexer indexer;
	  
	  String indexerId=index.getIndexerId();
	  log.debug("indexerId="+indexerId);
	  if(indexerService.existIndexer(index.getId())){
		  log.debug("Indexer exists");
	   
		  if (indexerId.contains("CensusIndexerMem")){ 
			  indexer=indexerService.getIndexer(index.getId());
			  indexer.build();
		  }
		  return "Building Index : Done";
	  }else{
		  return "No Index to Build";
	  }
	 
	  
	  }
	  
	  
	  @POST
	  @Path("/{id}/query")
	  @Produces(MediaType.TEXT_HTML)
	 
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED) public String
	  queryindex(@Form UploadForm form,@PathParam("id") String id,@Context
	  ServletContext context){
	  
	  IndexingEngine inengine = (IndexingEngine) context
	  .getAttribute(IndexingEngine.class.getName()); Injector injector =
	  (Injector) context.getAttribute(Injector.class .getName());
	  IndexServiceImpl indexService = injector
	  .getInstance(IndexServiceImpl.class);
	  
	  IndexerServiceImpl indexerService=injector.getInstance(IndexerServiceImpl.class);
	  
	  log.debug("query : Linear or Cluster Index");
	  
	  String fileurl=form.infile; 
	  File inputFile=null; 
	  String  adapter;
	  String extractor;
	  String measure; 
	  Indexer indexer;
	  
	  Index index=indexService.getIndex(id);
	  adapter=index.getAdapterId();
	  extractor=index.getExtractorId();
	  measure=index.getMeasureId();
	  
	  String indexerId=index.getIndexerId();
	  
	  if (indexerService.existIndexer(index.getId())) {
			indexer = indexerService.getIndexer(index.getId());
		} else {
			if (indexerId.contains("LinearIndexerMem")) {
				//indexer = new LinearIndexer(
				//		indexerService.getIndexerProcessor());
				indexer = new LinearIndexerMem();
			} else if(indexerId.contains("LinearIndexerDisk")){
				indexer=new LinearIndexerDisk();
			}
			else {
				//indexer = new CensusIndexer();
				indexer=new CensusIndexerMem();
			}
			
			indexer.setId(index.getId());
			indexerService.addIndexer(indexer);
		}
		
	  
	  
	  
	  List<Double> result=new ArrayList<Double>();
	  List<SearchResult> resultC=new ArrayList<SearchResult>();
	  
	  try { 
		  inputFile=ComparisonResource.getFile(fileurl);
	  //inputFile.getName() 
	  } catch (IOException e1) {  e1.printStackTrace(); }
	  
	  String content = new  String("<h3>Versus > Index Instance>Query Results</h3>" + "<ul>");
	  
	  
	  if (inputFile.isFile()) {
		  log.debug("isFile");
	  
	  try {
	  
	  resultC=inengine.queryIndex(inputFile, adapter, extractor, measure,
	  indexer); 
	  if(!resultC.isEmpty()){
		  for(int i=0;i<resultC.size()-1;i++){
			  log.debug("result docID: "+resultC.get(i).getDocId() +
					  "Proximity value:"+resultC.get(i).getProximity().getValue());
			  content+="<li>docID: "+resultC.get(i).getDocId()+"Proximity value: "+resultC.get(i).getProximity().getValue()+"</li>";
			  } 
		  } else{
	       log.debug("result id Empty"); } content+="</ul>";
	  
	  } catch (InterruptedException e) { // TODO Auto-generated catch block
	         e.printStackTrace();
	         } 
	  catch (ExecutionException e) {
	   e.printStackTrace(); 
	   }
	  
	  }
	  
	  return content; 
	  }
	 

	public static class UploadForm {
		@FormParam("infile")
		private String infile;

		@Override
		public String toString() {
			return "infile=" + infile;

		}

	}

	public static class IndexForm {

		@FormParam("Adapter")
		private String adapter;

		@FormParam("Extractor")
		private String extractor;

		@FormParam("Measure")
		private String measure;

		@FormParam("Indexer")
		private String indexer;

		@Override
		public String toString() {
			return "Adapter=" + adapter + "&Extractor=" + extractor
					+ "&Measure" + measure + "&Indexer=" + indexer;
		}
	}

}
