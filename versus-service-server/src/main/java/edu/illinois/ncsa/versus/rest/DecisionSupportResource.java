/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.Form;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.rest.ComparisonResource;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.restlet.DecisionSupport;
import edu.illinois.ncsa.versus.restlet.DecisionSupport.DSInfo;
import edu.illinois.ncsa.versus.restlet.DecisionSupport.DS_Status;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.SlavesManager;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonSubmitter;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.DecisionSupportServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * 
 * @author Luigi Marini
 * 
 */
@Path("/decisionSupport")
public class DecisionSupportResource {

	private static Log log = LogFactory.getLog(DecisionSupportResource.class);

	@GET
	@Produces("text/html")
	public String list(@Context ServletContext context) {

		// Guice storage
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		DecisionSupportServiceImpl dsService = injector
				.getInstance(DecisionSupportServiceImpl.class);
		Collection<DecisionSupport> dsCollection = dsService.listAll();

		if (dsCollection.size() == 0) {
			return "No decision support trials";
		} else {
			String content = new String("<h3>Versus > Decision Support</h3>"
					+ "<ul>");
			for (DecisionSupport ds : dsCollection) {
				String id = ds.getId();
				content += "<li><a href='/versus/api/decisionSupport/" + id
						+ "'>" + id + "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String submit(@Form DSForm form, @Context ServletContext context)
			throws IOException {

		// Guice storage
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());
		ExecutionEngine engine = (ExecutionEngine) context
				.getAttribute(ExecutionEngine.class.getName());
        SlavesManager slavesManager = (SlavesManager) context
                .getAttribute(SlavesManager.class.getName());
		DecisionSupportServiceImpl dsService = injector
				.getInstance(DecisionSupportServiceImpl.class);

		DecisionSupport ds = new DecisionSupport();

		ds.setId(UUID.randomUUID().toString());
		ds.setSimilarData(form.similarFiles); // array
												// of
												// similar
												// file
												// urls
		ds.setDissimilarData(form.dissimilarFiles); // array of
													// dissimilar
		
		// file
		// urls
		ds.setAdapterId(form.adapter);

		ds.setAvailableExtractors(new ArrayList<Extractor>(
				getExtractors(context)));
		ds.setAvailableMeasures(new ArrayList<Measure>(getMeasures(context)));
		ds.getSupportedMethods();
		ds.setStatus(DS_Status.STARTED);
		submitSimilarComparisons(ds, registry, engine);
		// add the service;
		dsService.addDecisionSupport(ds);
		// run the service
		ds.getBestPair();
		return ds.getId();
	}

//	@GET
//	@Path("/{id}")
//	@Produces("text/html")
//	public DecisionSupport getBean(@PathParam("id") String id,
//			@Context ServletContext context) {
//		Injector injector = (Injector) context.getAttribute(Injector.class
//				.getName());
//		DecisionSupportServiceImpl dsService = injector
//				.getInstance(DecisionSupportServiceImpl.class);
//		return dsService.getDecisionSupport(id);
//	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Map<String, Object> getJson(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		DecisionSupportServiceImpl dsService = injector
				.getInstance(DecisionSupportServiceImpl.class);
		DecisionSupport ds = dsService.getDecisionSupport(id);

		if(ds.getStatus() != DS_Status.RUNNING || ds.getStatus() != DS_Status.DONE){
			ds.getBestPair();
		}
		
		log.debug("get json");
		Map<String, Object> json = new HashMap<String, Object>();
		if (ds != null) {
			json.put("id", ds.getId());
			json.put("status", ds.getStatus());
			json.put("decidedMethod", ds.getDecidedMethod());
			json.put("rankedResults", ds.getBestResultsList());

		} else {
			json.put("Error", "Adapter not found");
		}
		return json;
	}
	
	

	/**
	 * 
	 * @param ds
	 * @param engine
	 * @param registry
	 * @throws IOException
	 */
	private void submitSimilarComparisons(DecisionSupport ds,
			CompareRegistry registry, ExecutionEngine engine)
			throws IOException {

		Injector injector = ServerApplication.getInjector();
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);

		ArrayList<String> similarFiles = new ArrayList<String>(
				ds.getSimilarData());
		ArrayList<String> dissimilarFiles = new ArrayList<String>(
				ds.getDissimilarData());
		ArrayList<DSInfo> decisionSupportData = ds.getDecisionSupportData();

		ArrayList<String> s_comparisonIds = new ArrayList<String>();
		ArrayList<String> d_comparisonIds = new ArrayList<String>();
		ComparisonResource a = new ComparisonResource();

		for (int x = 0; x < decisionSupportData.size(); x++) {

			// setup similar file comparisons
			while (!similarFiles.isEmpty()) {

				for (int j = 0; j < similarFiles.size()-1; j++) {

					String cid = comparisonService.findComparison(
							similarFiles.get(0), similarFiles.get(j),
							decisionSupportData.get(x).getAdapterID(),
							decisionSupportData.get(x).getExtractorID(),
							decisionSupportData.get(x).getMeasureID()); // check
																		// if
																		// current
																		// comparison
																		// exists.

					if (cid == null) {
                            		
                        String adapter = decisionSupportData.get(x).getAdapterID();
                        String extractor = decisionSupportData.get(x).getExtractorID();
                        String measure = decisionSupportData.get(x).getMeasureID();
                        String dataset1Name = similarFiles.get(0);
                        String dataset2Name =  similarFiles.get(j);
                        InputStream dataset1Stream;
                        InputStream dataset2Stream;
                        Comparison comparison = new Comparison(dataset1Name, dataset2Name, adapter, extractor, measure);
                        String id = comparison.getId();
                        if (registry.supportComparison(adapter, extractor, measure)) {
                            try {
                                dataset1Stream = new URL(dataset1Name).openStream();
                                dataset2Stream = new URL(dataset2Name).openStream();
                                ComparisonSubmitter submitter = new ComparisonSubmitter(
                                        registry, engine, null, 
                                        comparison, dataset1Stream, dataset2Stream);
                                submitter.submit();
                            } catch (IOException e) {
                            log.error("Internal error writing to disk", e);
                            }catch (NoSlaveAvailableException e) {
                                log.error("Error creating local job", e);
                            }
                        }
						
						s_comparisonIds.add(id);
					} else {
						s_comparisonIds.add(cid);
					}
				}
				similarFiles.remove(0);
			}
			// setup dissimilar file comparisons
			while (!dissimilarFiles.isEmpty()) {
				for (int j = 0; j < dissimilarFiles.size()-1; j++) {

					String cid = comparisonService.findComparison(
							dissimilarFiles.get(0), dissimilarFiles.get(j),
							decisionSupportData.get(x).getAdapterID(),
							decisionSupportData.get(x).getExtractorID(),
							decisionSupportData.get(x).getMeasureID()); // check
																		// if
																		// current
																		// comparison
																		// exists.

					if (cid == null) {
                        String adapter = decisionSupportData.get(x).getAdapterID();
                        String extractor = decisionSupportData.get(x).getExtractorID();
                        String measure = decisionSupportData.get(x).getMeasureID();
                        String dataset1Name = similarFiles.get(0);
                        String dataset2Name =  similarFiles.get(j);
                        InputStream dataset1Stream;
                        InputStream dataset2Stream;
                        Comparison comparison = new Comparison(dataset1Name, dataset2Name, adapter, extractor, measure);
                        String id = comparison.getId();
                        if (registry.supportComparison(adapter, extractor, measure)) {
                            try {
                                dataset1Stream = new URL(dataset1Name).openStream();
                                dataset2Stream = new URL(dataset2Name).openStream();
                                ComparisonSubmitter submitter = new ComparisonSubmitter(
                                        registry, engine, null, 
                                        comparison, dataset1Stream, dataset2Stream);
                                submitter.submit();
                            } catch (IOException e) {
                            log.error("Internal error writing to disk", e);
                            }catch (NoSlaveAvailableException e) {
                                log.error("Error creating local job", e);
                            }
                        }
						d_comparisonIds.add(id);
					} else {
						d_comparisonIds.add(cid);
					}
				}
				dissimilarFiles.remove(0);
			}
			similarFiles = new ArrayList<String>(ds.getSimilarData());
			dissimilarFiles = new ArrayList<String>(ds.getDissimilarData());
			// add similarFiles and dissimilarFiles to the corresponding DSInfo
			decisionSupportData.get(x).setSimilarComparisons(s_comparisonIds);
			decisionSupportData.get(x)
					.setDissimilarComparisons(d_comparisonIds);
		}
		ds.setDecisionSupportData(decisionSupportData);
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

	public static class DSForm {

		@FormParam("adapter")
		private String adapter;

		@FormParam("similarFiles")
		private List<String> similarFiles;

		@FormParam("dissimilarFiles")
		private List<String> dissimilarFiles;

		@Override
		public String toString() {
			return "adapter=" + adapter + "&similarFiles=" + similarFiles
					+ "&dissimilarFiles=" + dissimilarFiles;
		}

	}

}
