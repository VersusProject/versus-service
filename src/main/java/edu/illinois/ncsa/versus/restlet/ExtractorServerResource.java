/**
 *
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.registry.CompareRegistry;

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
            StringBuilder sb = new StringBuilder(128);
            sb.append("Name: ").append(extractor.getName()).append("<br>");
            sb.append("Type: ").append(extractor.getClass().getName()).append("<br>");
            sb.append("Supported Adapters:<br>");
            for (Class<? extends Adapter> adapter : getAvailableAdapters(extractor)) {
                sb.append('\t').append(adapter.getName()).append("<br>");
            }
            sb.append("Supported Feature: ").append(extractor.getFeatureType()).append("<br>");
            return new StringRepresentation(sb, MediaType.TEXT_HTML);
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
            elementType.appendChild(document.createTextNode(extractor.getClass().getName()));
            elementExtractor.appendChild(elementType);
            // Supported adapters
            Element elementSupportedAdapters = document.createElement("supportedAdapters");
            elementExtractor.appendChild(elementSupportedAdapters);
            for (Class<? extends Adapter> adapter : getAvailableAdapters(extractor)) {
                Element eleAdapter = document.createElement("adapter");
                eleAdapter.appendChild(document.createTextNode(adapter.getName()));
                elementExtractor.appendChild(eleAdapter);
            }
            // Supported feature
            Element elementSupportedFeature = document.createElement("supportedFeature");
            elementType.appendChild(document.createTextNode(extractor.getFeatureType().getName()));
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
            JSONArray adaptersArray = new JSONArray();
            for (Class<? extends Adapter> adapter : getAvailableAdapters(extractor)) {
                JSONObject adapterObject = new JSONObject();
                adapterObject.put("adapter", adapter.getName());
                adaptersArray.put(adapterObject);
            }
            jsonObject.put("supportedAdapters", adaptersArray);
            jsonObject.put("supportedFeature", extractor.getFeatureType().getName());
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
        Collection<Extractor> extractors = ((ServerApplication) getApplication()).getExtractors();
        for (Extractor extractor : extractors) {
            if (extractor.getClass().getName().equals(id)) {
                return extractor;
            }
        }
        throw new NotFoundException();
    }

    private Set<Class<? extends Adapter>> getAvailableAdapters(Extractor extractor) {
        CompareRegistry registry = ((ServerApplication) getApplication()).getRegistry();
        Collection<Adapter> adapters = registry.getAvailableAdapters(extractor);
        Set result = new HashSet(adapters.size());
        for (Adapter adapter : adapters) {
            result.add(adapter.getClass());
        }
        return result;
    }
}
