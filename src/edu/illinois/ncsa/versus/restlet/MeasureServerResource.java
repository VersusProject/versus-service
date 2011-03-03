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

import edu.illinois.ncsa.versus.measure.Measure;

/**
 * Single measure.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class MeasureServerResource extends ServerResource {

	@Get("html")
	public Representation asText() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Measure measure = getMeasure(id);
			String text = "";
			text += "Name: " + measure.getName() + "<br>";
			text += "Type: " + measure.getType().getName() + "<br>";
			text += "Supported Feature: " + measure.getFeatureType() + "<br>";
			return new StringRepresentation(text, MediaType.TEXT_HTML);
		} catch (NotFoundException e) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Measure " + id + " not found",
					MediaType.TEXT_PLAIN);
		}
	}

	@Get("xml")
	public Representation asXML() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Measure measure = getMeasure(id);
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			Document document = representation.getDocument();
			Element elementMeasure = document.createElement("measure");
			document.appendChild(elementMeasure);
			// name
			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(measure.getName()));
			elementMeasure.appendChild(name);
			// type
			Element elementType = document.createElement("id");
			elementType.appendChild(document.createTextNode(measure.getClass()
					.getName()));
			elementMeasure.appendChild(elementType);
			// mime types
			Element elementSupportedFeature = document
					.createElement("supportedFeature");
			elementType.appendChild(document.createTextNode(measure
					.getFeatureType()));
			elementMeasure.appendChild(elementSupportedFeature);
			// normalize
			document.normalizeDocument();
			return representation;
		} catch (NotFoundException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Measure " + id + " not found",
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
			Measure measure = getMeasure(id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", measure.getName());
			jsonObject.put("id", measure.getClass().getName());
			jsonObject.put("supportedFeature", measure.getFeatureType());
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

	private Measure getMeasure(String id) throws NotFoundException {
		Collection<Measure> measures = ((ServerApplication) getApplication())
				.getMeasures();
		for (Measure measure : measures) {
			if (measure.getClass().getName().equals(id)) {
				return measure;
			}
		}
		throw new NotFoundException();
	}
}
