/**
 *
 */
package edu.illinois.ncsa.versus.restlet.adapter;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 * Single adapter.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class AdapterServerResource extends ServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/adapters/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public AdapterDescriptor retrieve() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        try {
            return ((ServerApplication) getApplication()).getAdapter(id);
        } catch (NotFoundException ex) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }
    }

    @Get("html")
    public Representation asHtml() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        AdapterDescriptor adapter;
        try {
            adapter = ((ServerApplication) getApplication()).getAdapter(id);
        } catch (NotFoundException e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Adapter " + id + " not found",
                    MediaType.TEXT_HTML);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(adapter.getName()).append("<br>");
        sb.append("Type: ").append(adapter.getType()).append("<br>");
        sb.append("Supported Media Types:<br>");
        for (String type : adapter.getSupportedMediaTypes()) {
            sb.append('\t').append(type).append("<br>");
        }
        return new StringRepresentation(sb, MediaType.TEXT_HTML);
    }
}
