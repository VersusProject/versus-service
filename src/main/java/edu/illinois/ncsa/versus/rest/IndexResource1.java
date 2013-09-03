package edu.illinois.ncsa.versus.rest;

/*
 * Index Resource with map object implementation
 * 
 * @author Smruti Padhy
 * */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import edu.illinois.ncsa.versus.engine.impl.IndexingEngine;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

import edu.illinois.ncsa.versus.store.IndexServiceImpl;
import edu.illinois.ncsa.versus.store.IndexerServiceImpl;
import edu.illinois.ncsa.versus.store.MapObjectServiceImpl;


@Path("/index")
public class IndexResource1 {
	private static Log log = LogFactory.getLog(IndexResource1.class);

	
	@GET
	@Produces("application/json")
	public List<Map<String, String>> listJSON(@Context ServletContext context) {

		// Guice storage
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);
		
        
        
		Collection<Index> indexCollection = indexService.listAll();

		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		if (indexCollection.size() == 0) {
			//return "No Index running";
			return list;
		} else {
			
			for (Index in : indexCollection) {
				String id = in.getId();
					Map<String,String> json=new HashMap<String,String>();
					String extract=in.getExtractorId().substring(in.getExtractorId().lastIndexOf(".")+1);
					log.debug("extractor id="+ in.getExtractorId()+"  extract"+extract);
					String measure=in.getMeasureId().substring(in.getMeasureId().lastIndexOf(".")+1);
					String indexer=in.getIndexerId().substring(in.getIndexerId().lastIndexOf(".")+1);
					json.put("indexID",id);
					json.put("MIMEtype", "image");
					json.put("Extractor", extract);
					json.put("Measure", measure);
					json.put("Indexer", in.getIndexerId());
					list.add(json);
			}
			
			return list;
		}
	}

	
	/*@GET
	@Produces("text/html")
	public String listHTML(@Context ServletContext context) {

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
	}*/

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String submit(@Form IndexForm form, @Context ServletContext context)
			throws IOException {

		//IndexingEngine inengine = (IndexingEngine) context
		//		.getAttribute(IndexingEngine.class.getName());

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);

		//IndexerServiceImpl indexerService = injector
		//		.getInstance(IndexerServiceImpl.class);

		Index index = new Index();
		//Indexer indexer;

		index.setId(UUID.randomUUID().toString());

		index.setAdapterId(form.adapter);
		index.setExtractorId(form.extractor);
		index.setMeasureId(form.measure);
		index.setIndexerId(form.indexer);
        		
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
		
		//MapServiceImpl mapService=injector.getInstance(MapServiceImpl.class);
		MapObjectServiceImpl mapObjectService=injector.getInstance(MapObjectServiceImpl.class);
		
		//IdMap m=mapService.getMap();
        
		Index index = indexService.getIndex(id);
		String indexerId = index.getIndexerId();

		Indexer indexer;
		// indexerService.getIndexer(id);

		if (indexerService.existIndexer(index.getId())) {
			indexer = indexerService.getIndexer(index.getId());
		} else {
			if (indexerId.contains("LinearIndexerMem")) {
				// indexer = new LinearIndexer(
				// indexerService.getIndexerProcessor());
				indexer = new LinearIndexerMem();
			} else if (indexerId.contains("LinearIndexerDisk")) {
				indexer = new LinearIndexerDisk();
			} else if(indexerId.contains("ClusterLinearIndexer")){
				indexer=new ClusterLinearIndexer();
			} 
			else {
				indexer = new CensusIndexerDisk();
			}

			indexer.setId(index.getId());
			indexerService.addIndexer(indexer);
		}

		String content = new String("<h3>Versus > Index Instance</h3>" + "<ul>");
		content += "<li>" + index.getAdapterId() + "</li>";
		content += "<li>" + index.getExtractorId() + "</li>";
		content += "<li>" + index.getMeasureId() + "</li>";
		content += "<li>" + index.getIndexerId() + "</li>";
         
		String id2="";
		/* search for the indexer based on id and list all file ids stored on it */
		if (indexer.getIdentifiers() == null) {
			//if (indexer.getIdentifiers().isEmpty()) {
			// if (indexer.getIdentifiers().size() == 0) {
			content += "<li>No Files in the index</li>";
			content += "</ul>";
			return content;
		} else {

			content += "<li> List of File Ids</li>";
			// for (String id1 : index.getindexHash().keySet()) {
			for (String id1 : indexer.getIdentifiers()) {
               // id2=m.getMId(id1);
				id2=mapObjectService.getMapObject(id1).getmediciId();
				content += "<li>" + id2 + "    "+id1+" </li>";
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
		
		IndexCounter count = (IndexCounter) context
				.getAttribute(IndexCounter.class.getName());
		
		MapObjectServiceImpl mapObjectService=injector.getInstance(MapObjectServiceImpl.class);
		
		MapObject mo=new MapObject();
		
		int rlen=2;
		
		String fileurl = form.infile;
		File inputFile = null;
		
		Index index = indexService.getIndex(id);
		String indexerId = index.getIndexerId();
		Indexer indexer;
		
		if (indexerService.existIndexer(index.getId())) {
			indexer = indexerService.getIndexer(index.getId());
		if(!count.getCounter().containsKey(index.getId()))
					count.setCount(index.getId());
			
		} else {
			if (indexerId.contains("LinearIndexerMem")) {
				indexer = new LinearIndexerMem();
			} else if (indexerId.contains("LinearIndexerDisk")) {
				indexer = new LinearIndexerDisk();
			} else if(indexerId.contains("ClusterLinearIndexer")){
				indexer=new ClusterLinearIndexer();
			}else {
				indexer = new CensusIndexerDisk();
			}
            count.setCount(index.getId());
			indexer.setId(index.getId());
			indexerService.addIndexer(indexer);
			
		}
		String adapter = index.getAdapterId();
		String extractor = index.getExtractorId();
		
		try {
			/*int startin=fileurl.lastIndexOf("/");
			int endin=fileurl.lastIndexOf("?");
			String fid=fileurl.substring(startin+1, endin);
			log.debug("fid="+fid);*/
			inputFile = ComparisonResource.getFile(fileurl);
						
			// inputFile.getName()
			//inputFile.getName(),
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (inputFile.isFile()) {
			log.debug("isFile");

			mo.setversusId(inputFile.getName());
			mo.setmediciId(fileurl);
			mo.setindexId(index.getId());
			mo.setbuildStatus("false");
						
			mapObjectService.addMapObject(mo);
			try {
				inengine.addDocument(inputFile, adapter, extractor, indexer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          if(count.getCount(index.getId())>=rlen){
        	 buildindex(index.getId(), context) ;
        	 log.debug("Building");
        	 count.resetCount(index.getId());
          }
          else{
        	  count.increment(index.getId());
            }
		}

		return "Done";

	}

	@GET
	@Path("/{id}/build")
	@Produces("text/html")
	public String buildindex(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);
		IndexerServiceImpl indexerService = injector
				.getInstance(IndexerServiceImpl.class);

      //  MapServiceImpl mapService=injector.getInstance(MapServiceImpl.class);
		MapObjectServiceImpl mapObjectService=injector.getInstance(MapObjectServiceImpl.class);
		/*IdMap m=mapService.getMap();
		mapService.addMap(m);*/
		
		Index index = indexService.getIndex(id);
		Indexer indexer;

		String indexerId = index.getIndexerId();
		String buildStatus="";
		
		log.debug("indexerId=" + indexerId);
		if (indexerService.existIndexer(index.getId())) {
			log.debug("Indexer exists");

			// if (indexerId.contains("CensusIndexerMem")) {
			indexer = indexerService.getIndexer(index.getId());
			indexer.build();
			// }
			log.debug("Building Done");
			buildStatus="true";
			mapObjectService.updateBuildStatus(index.getId(),buildStatus );
			return "Building Index : Done";
			// } else {
			// return "No Index to Build";
		}
		return "No Index to Build";
	}

	@POST
	@Path("/{id}/query")
	// @Produces(MediaType.TEXT_HTML)
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public List<Map<String, Object>> queryindex(@Form UploadForm form,
			@PathParam("id") String id, @Context ServletContext context) {

		IndexingEngine inengine = (IndexingEngine) context
				.getAttribute(IndexingEngine.class.getName());
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		IndexServiceImpl indexService = injector
				.getInstance(IndexServiceImpl.class);

		IndexerServiceImpl indexerService = injector
				.getInstance(IndexerServiceImpl.class);
		
		MapObjectServiceImpl mapObjectService=injector.getInstance(MapObjectServiceImpl.class);
		
		log.debug("query : Linear or Cluster Index");

		String fileurl = form.infile;
		File inputFile = null;
		String adapter;
		String extractor;
		String measure;
		Indexer indexer;

		Index index = indexService.getIndex(id);
		adapter = index.getAdapterId();
		extractor = index.getExtractorId();
		measure = index.getMeasureId();

		String indexerId = index.getIndexerId();

		if (indexerService.existIndexer(index.getId())) {
			indexer = indexerService.getIndexer(index.getId());
		} else {
			if (indexerId.contains("LinearIndexerMem")) {
				indexer = new LinearIndexerMem();
			} else if (indexerId.contains("LinearIndexerDisk")) {
				indexer = new LinearIndexerDisk();
			} else if(indexerId.contains("ClusterLinearIndexer")){
                 indexer= new ClusterLinearIndexer();  				
			}
			else {
				// indexer = new CensusIndexer();
				indexer = new CensusIndexerDisk();
			}

			indexer.setId(index.getId());
			indexerService.addIndexer(indexer);
		}

		List<Double> result = new ArrayList<Double>();
		List<SearchResult> resultC = new ArrayList<SearchResult>();

		try {
			inputFile = ComparisonResource.getFile(fileurl);
			// inputFile.getName()
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		
		if (inputFile.isFile()) {
			log.debug("isFile");

			try {

				resultC = inengine.queryIndex(inputFile, adapter, extractor,
						measure, indexer);

				// Result result1;

				if (!resultC.isEmpty()) {
					Collections.sort(resultC, new Comparator<SearchResult>() {
						public int compare(SearchResult r1, SearchResult r2) {
							return ((Double) r1.getProximity().getValue())
									.compareTo((Double) r2.getProximity()
											.getValue());
						}
					});
					int top5;
					if(resultC.size()>12)
						top5=12;
					else
						top5=resultC.size();
					//for (int i = 0; i < resultC.size() - 1; i++) {
					for (int i = 0; i < top5 - 1; i++) {
						Map<String, Object> json = new HashMap<String, Object>();
						if (!resultC.get(i).getDocId().equals("0")) {
							json.put("docID", mapObjectService.getMapObject(resultC.get(i).getDocId()).getmediciId());
							json.put("proximity", resultC.get(i).getProximity()
									.getValue());
							results.add(json);

							
						}
					}
				} else {
					log.debug("result id Empty");
				}
				// content += "</ul>";

			} catch (InterruptedException e) { // TODO Auto-generated catch
												// block
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}

		// return content;
		return results;
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
