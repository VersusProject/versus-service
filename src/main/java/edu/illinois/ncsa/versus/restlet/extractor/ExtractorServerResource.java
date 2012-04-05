/**
 *
 */
package edu.illinois.ncsa.versus.restlet.extractor;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 * Single extractor.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ExtractorServerResource extends ServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/extractors/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public ExtractorDescriptor retrieve() {
        String id = (String) getRequest().getAttributes().get("id");
        try {
            return ((ServerApplication) getApplication()).getExtractor(id);
        } catch (NotFoundException ex) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }
    }

    @Get("html")
    public Representation asHtml() {
        String id = (String) getRequest().getAttributes().get("id");
        try {
            ExtractorDescriptor extractor =
                    ((ServerApplication) getApplication()).getExtractor(id);
            StringBuilder sb = new StringBuilder(128);
            sb.append("Name: ").append(extractor.getName()).append("<br>");
            sb.append("Type: ").append(extractor.getType()).append("<br>");
            sb.append("Supported Adapters:<br>");
            for (String adapter : extractor.getSupportedAdapters()) {
                sb.append('\t').append(adapter).append("<br>");
            }
            sb.append("Supported Feature: ").append(
                    extractor.getSupportedFeature()).append("<br>");
            return new StringRepresentation(sb, MediaType.TEXT_HTML);
        } catch (NotFoundException e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Extractor " + id + " not found",
                    MediaType.TEXT_HTML);
        }
    }
}
