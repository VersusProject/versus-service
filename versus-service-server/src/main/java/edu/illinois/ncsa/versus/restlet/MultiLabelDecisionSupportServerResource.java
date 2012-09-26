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

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport.MLDS_Status;
import edu.illinois.ncsa.versus.store.MultiLabelDecisionSupportServiceImpl;



public class MultiLabelDecisionSupportServerResource extends VersusServerResource {
	
	@Get("xml")
	public Representation asXML() {		
	
		String id                                  				 = (String) getRequest().getAttributes().get("id");
		Injector injector                           		     = ServerApplication.getInjector();
		MultiLabelDecisionSupportServiceImpl distributionService = injector.getInstance(MultiLabelDecisionSupportServiceImpl.class);
		MultiLabelDecisionSupport mlds                  		 = distributionService.getMultiLabelDecisionSupport(id);
		
		if( ( !mlds.checkIfComputationComplete()) || (mlds.getStatus() != MLDS_Status.RUNNING) ){
			mlds.getBestPair();
		}
		
		if (mlds != null) {
			try {
				DomRepresentation representation = new DomRepresentation(MediaType.TEXT_XML);
				Document document                = representation.getDocument();
				Element elementDistribution      = document.createElement("MultiLabelDecisionSupport");
				document.appendChild(elementDistribution);
				
				// id
				Element elementId = document.createElement("id");
				elementId.appendChild(document.createTextNode(mlds.getId()));
				elementDistribution.appendChild(elementId);
				
				// status
				Element statusId = document.createElement("status");
				String status;
				if (mlds.getStatus() != null) {
					status = mlds.getStatus().name();
				} else {
					status = "N/A";
				}
				statusId.appendChild(document.createTextNode(status));
				elementDistribution.appendChild(statusId);
				
				// method chosen
				Element mc = document.createElement("methodChosen");
				mc.appendChild(document.createTextNode(mlds.getMethod()));
				elementDistribution.appendChild(mc);
				
				// adapter
				Element adapter = document.createElement("adapter");
				adapter.appendChild(document.createTextNode(mlds.getAdapterId()));
				elementDistribution.appendChild(adapter);
				
				// extractors
				Element extractors = document.createElement("extractors");
				extractors.appendChild(document.createTextNode(mlds.availableExtractors2String()));
				elementDistribution.appendChild(extractors);
				
				// measures
				Element measures = document.createElement("measures");
				measures.appendChild(document.createTextNode(mlds.availableMeasures2String()));
				elementDistribution.appendChild(measures);
				
				// bestPair
				Element bestPair = document.createElement("bestPair");
				bestPair.appendChild(document.createTextNode(mlds.getDecidedMethod()));
				elementDistribution.appendChild(bestPair);
				
				// best methods
				Element bestMethods = document.createElement("bestMethods");
				bestMethods.appendChild(document.createTextNode(mlds.getBestResultsList()));
				elementDistribution.appendChild(bestMethods);
				
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