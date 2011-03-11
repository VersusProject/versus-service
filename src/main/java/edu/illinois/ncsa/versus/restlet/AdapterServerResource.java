/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONArray;
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

import edu.illinois.ncsa.versus.adapter.Adapter;

/**
 * Single adapter.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class AdapterServerResource extends ServerResource {

	@Get("html")
	public Representation asText() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Adapter adapter = getAdapter(id);
			String text = "";
			text += "Name: " + adapter.getName() + "<br>";
			text += "Type: " + adapter.getClass().getName() + "<br>";
			text += "Supported Media Types:<br>";
			for (String type : adapter.getSupportedMediaTypes()) {
				text += "\t" + type + "<br>";
			}
			return new StringRepresentation(text, MediaType.TEXT_HTML);
		} catch (NotFoundException e) {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Adapter " + id + " not found",
					MediaType.TEXT_HTML);
		}
	}

	@Get("xml")
	public Representation asXML() {
		String id = (String) getRequest().getAttributes().get("id");
		try {
			Adapter adapter = getAdapter(id);
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			Document document = representation.getDocument();
			Element elementAdapter = document.createElement("adapter");
			document.appendChild(elementAdapter);
			// name
			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(adapter.getName()));
			elementAdapter.appendChild(name);
			// type
			Element elementType = document.createElement("id");
			elementType.appendChild(document.createTextNode(adapter.getClass()
					.getName()));
			elementAdapter.appendChild(elementType);
			// mime types
			Element elementSupportedTypes = document
					.createElement("supportedTypes");
			elementAdapter.appendChild(elementSupportedTypes);
			for (String type : adapter.getSupportedMediaTypes()) {
				Element eleType = document.createElement("type");
				eleType.appendChild(document.createTextNode(type));
				elementSupportedTypes.appendChild(eleType);
			}
			// normalize
			document.normalizeDocument();
			return representation;
		} catch (NotFoundException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Adapter " + id + " not found",
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
			Adapter adapter = getAdapter(id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", adapter.getName());
			jsonObject.put("id", adapter.getClass().getName());
			JSONArray typeArray = new JSONArray();
			for (String type : adapter.getSupportedMediaTypes()) {
				JSONObject typeObject = new JSONObject();
				typeObject.put("type", type);
				typeArray.put(typeObject);
			}
			jsonObject.put("supportedTypes", typeArray);
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

	private Adapter getAdapter(String id) throws NotFoundException {
		Collection<Adapter> adapters = ((ServerApplication) getApplication())
				.getAdapters();
		for (Adapter adapter : adapters) {
			if (adapter.getClass().getName().equals(id)) {
				return adapter;
			}
		}
		throw new NotFoundException();
	}
}
