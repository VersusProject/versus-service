/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.DistributionServiceImpl;



public class DistributionServerResource extends VersusServerResource {
	
	
	@Get
	public Distribution getBean() {
		
		String id                                   = (String) getRequest().getAttributes().get("id");
		Injector injector                           = ServerApplication.getInjector();
		DistributionServiceImpl distributionService = injector.getInstance(DistributionServiceImpl.class);		
		return distributionService.getDistribution(id);
	}	
	
	@Get("json")
	public Representation asJSON(){

		String id                                   = (String) getRequest().getAttributes().get("id");
		Injector injector                           = ServerApplication.getInjector();
		DistributionServiceImpl distributionService = injector.getInstance(DistributionServiceImpl.class);
		Distribution distribution                   = distributionService.getDistribution(id);
		
		try {
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("id", distribution.getId() );
			jsonObject.put("adapter",distribution.getAdapterId() );
			jsonObject.put("extractor",distribution.getExtractorId() );
			jsonObject.put("measure",distribution.getMeasureId() );
			jsonObject.put("mean",	distribution.getMean() );
			jsonObject.put("standardDeviation",distribution.getDeviation() );	
			jsonObject.put("status", distribution.getStatus() );
			
			//TODO: calculate statistics..?
			
			String[] data = distribution.getDataset();
			for(int i=0; i<data.length; i++){
				jsonObject.put("file "+i, data[i]);
			}
						
			return new JsonRepresentation(jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Error creating json",	MediaType.TEXT_HTML);
		}
	}
	
//	@Get("html")
//	public Representation asHTML() {
//			
//		String id                                   = (String) getRequest().getAttributes().get("id");
//		Injector injector                           = ServerApplication.getInjector();
//		DistributionServiceImpl distributionService = injector.getInstance(DistributionServiceImpl.class);
//		Distribution distribution                   = distributionService.getDistribution(id);
//
//		if (distribution != null) {
//			
//			String status;
//			if (distribution.getStatus() != null) {
//				status = distribution.getStatus().name();
//			} else {
//				status = "N/A";
//			}
//			
//			
//			distribution.calculateStatistics();
//			
//			String content = new String("<h3>Distribution</h3>"	+ "<ul>");
//			content += "<li>ID:" + id + "</li>";
//			content += "<li>Status:" + status + "</li>";
//			content += "<li>Mean:" + distribution.getMean() + "</li>";
//			content += "<li>Sigma:" + distribution.getDeviation() + "</li>";
//			content += "</ul>";
//			Representation representation = new StringRepresentation(content, MediaType.TEXT_HTML);
//			return representation;
//		}
//		else{
//			Representation representation = new StringRepresentation("No distributions", MediaType.TEXT_HTML);
//			return representation;
//		}
//	}
	
	
	@Get("xml")
	public Representation asXML() {		
	
		String id                                   = (String) getRequest().getAttributes().get("id");
		Injector injector                           = ServerApplication.getInjector();
		DistributionServiceImpl distributionService = injector.getInstance(DistributionServiceImpl.class);
		Distribution distribution                   = distributionService.getDistribution(id);

		if (distribution != null) {
			try {
				DomRepresentation representation = new DomRepresentation(MediaType.TEXT_XML);
				Document document                = representation.getDocument();
				Element elementDistribution      = document.createElement("distribution");
				document.appendChild(elementDistribution);
				
				// id
				Element elementId = document.createElement("id");
				elementId.appendChild(document.createTextNode(distribution.getId()));
				elementDistribution.appendChild(elementId);
				
				// status
				Element statusId = document.createElement("status");
				String status;
				if (distribution.getStatus() != null) {
					status = distribution.getStatus().name();
				} else {
					status = "N/A";
				}
				statusId.appendChild(document.createTextNode(status));
				elementDistribution.appendChild(statusId);
				
				distribution.calculateStatistics();
				
				// mean
				Element meanId = document.createElement("mean");
				if (distribution.getMean() != null) {
					meanId.appendChild(document.createTextNode(distribution.getMean() ));
				} else {
					meanId.appendChild(document.createTextNode("N/A"));
				}
				elementDistribution.appendChild(meanId);
				
				// std dev
				Element sigmaId = document.createElement("sigma");
				if (distribution.getDeviation() != null) {
					sigmaId.appendChild(document.createTextNode(distribution.getDeviation() ));
				} else {
					sigmaId.appendChild(document.createTextNode("N/A"));
				}
				elementDistribution.appendChild(sigmaId);
				
				// dataset
				Element dataset = document.createElement("dataset");
				dataset.appendChild(document.createTextNode(distribution.dataset2String()));
				elementDistribution.appendChild(dataset);
				
				// adapter
				Element adapter = document.createElement("adapter");
				adapter.appendChild(document.createTextNode(distribution.getAdapterId()));
				elementDistribution.appendChild(adapter);
				
				// extractor
				Element extractor = document.createElement("extractor");
				extractor.appendChild(document.createTextNode(distribution.getExtractorId()));
				elementDistribution.appendChild(extractor);
				
				// measure
				Element measure = document.createElement("measure");
				measure.appendChild(document.createTextNode(distribution.getMeasureId()));
				elementDistribution.appendChild(measure);
				
				// normalize
				document.normalizeDocument();
				return representation;
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Error creating distribution xml",e);
				e.printStackTrace();
			}
		} else {
			return new StringRepresentation("A distribution with id " + id + " does not exist.");
		}
		return new StringRepresentation("Error retrieving distribution with id " + id);
	}
	
}
