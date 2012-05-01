/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.illinois.ncsa.versus.extract.Extractor;

/**
 * Single extractor.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class ExtractorServerResource extends ServerResource {

	@Get("html")
	public Representation asText() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Extractor extractor = getExtractor(id);
			String text = "";
			text += "Name: " + extractor.getName() + "<br>";
			text += "Type: " + extractor.getClass().getName() + "<br>";
			text += "Supported Feature: " + extractor.getFeatureType() + "<br>";
			return new StringRepresentation(text, MediaType.TEXT_HTML);
		} catch (NotFoundException e) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Extractor " + id + " not found",
					MediaType.TEXT_HTML);
		}
	}

	@Get("xml")
	public Representation asXML() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Extractor extractor = getExtractor(id);
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			Document document = representation.getDocument();
			Element elementExtractor = document.createElement("extractor");
			document.appendChild(elementExtractor);
			// name
			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(extractor.getName()));
			elementExtractor.appendChild(name);
			// type
			Element elementType = document.createElement("id");
			elementType.appendChild(document.createTextNode(extractor
					.getClass().getName()));
			elementExtractor.appendChild(elementType);
			// mime types
			Element elementSupportedFeature = document
					.createElement("supportedFeature");
			elementType.appendChild(document.createTextNode(extractor
					.getFeatureType().getName()));
			elementExtractor.appendChild(elementSupportedFeature);
			// normalize
			document.normalizeDocument();
			return representation;
		} catch (NotFoundException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Extractor " + id + " not found",
					MediaType.TEXT_XML);
		} catch (IOException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Error creating xml document",
					MediaType.TEXT_XML);
		}
	}

	@Get("json")
	public Representation asJSON() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Extractor extractor = getExtractor(id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", extractor.getName());
			jsonObject.put("id", extractor.getClass().getName());
			jsonObject.put("supportedFeature", extractor.getFeatureType()
					.getName());
			return new JsonRepresentation(jsonObject);
		} catch (NotFoundException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Error creating json",
					MediaType.TEXT_HTML);
		} catch (JSONException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Error creating json",
					MediaType.TEXT_HTML);
		}
	}

	private Extractor getExtractor(String id) throws NotFoundException {
		Collection<Extractor> extractors = ((ServerApplication) getApplication())
				.getExtractors();
		for (Extractor extractor : extractors) {
			if (extractor.getClass().getName().equals(id)) {
				return extractor;
			}
		}
		throw new NotFoundException();
	}
	
}
