/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.DecisionSupportServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

public class DecisionSupportServerResource extends VersusServerResource {
	
	
	@Get
	public DecisionSupport getBean() {
		
		String id                            = (String) getRequest().getAttributes().get("id");
		Injector injector                    = Guice.createInjector(new RepositoryModule());
		DecisionSupportServiceImpl dsService = injector.getInstance(DecisionSupportServiceImpl.class);		
		return dsService.getDecisionSupport(id);
	}	
	
	//TODO JSON object
	
	@Get("xml")
	public Representation asXML() {		
	
		String id                            = (String) getRequest().getAttributes().get("id");
		Injector injector                    = Guice.createInjector(new RepositoryModule());
		DecisionSupportServiceImpl dsService = injector.getInstance(DecisionSupportServiceImpl.class);		
		DecisionSupport ds                   = dsService.getDecisionSupport(id);
		ds.getBestPair();
		
		if (ds != null) {
			try {
				DomRepresentation representation = new DomRepresentation(MediaType.TEXT_XML);
				Document document                = representation.getDocument();
				Element elementDistribution      = document.createElement("decisionSupport");
				document.appendChild(elementDistribution);
				
				// id
				Element elementId = document.createElement("id");
				elementId.appendChild(document.createTextNode(ds.getId()));
				elementDistribution.appendChild(elementId);
				
				// status
				Element statusId = document.createElement("status");
				String status;
				if (ds.getStatus() != null) {
					status = ds.getStatus().name();
				} else {
					status = "N/A";
				}
				statusId.appendChild(document.createTextNode(status));
				elementDistribution.appendChild(statusId);
				
				ds.getBestPair();
					
				// similar dataset
				Element s_dataset = document.createElement("similarData");
				s_dataset.appendChild(document.createTextNode(ds.similarData2String()));
				elementDistribution.appendChild(s_dataset);
				
				// dissimilar dataset
				Element d_dataset = document.createElement("dissimilarData");
				d_dataset.appendChild(document.createTextNode(ds.dissimilarData2String()));
				elementDistribution.appendChild(d_dataset);
				
				// adapter
				Element adapter = document.createElement("adapter");
				adapter.appendChild(document.createTextNode(ds.getAdapterId()));
				elementDistribution.appendChild(adapter);
				
				// extractors
				Element extractors = document.createElement("extractors");
				extractors.appendChild(document.createTextNode(ds.availableExtractors2String()));
				elementDistribution.appendChild(extractors);
				
				// measures
				Element measures = document.createElement("measures");
				measures.appendChild(document.createTextNode(ds.availableMeasures2String()));
				elementDistribution.appendChild(measures);
				
				// method
				Element computedMethod = document.createElement("computedMethod");
				computedMethod.appendChild(document.createTextNode(ds.getDecidedMethod()));
				elementDistribution.appendChild(computedMethod);
				
				// normalize
				document.normalizeDocument();
				return representation;
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Error creating Decision Support xml",e);
				e.printStackTrace();
			}
		} else {
			return new StringRepresentation("A Decision Support call with id " + id + " does not exist.");
		}
		return new StringRepresentation("Error retrieving Decision Support with id " + id);
	}
	
}
