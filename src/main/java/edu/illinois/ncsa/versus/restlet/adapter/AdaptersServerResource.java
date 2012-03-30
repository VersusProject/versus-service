/**
 *
 */
package edu.illinois.ncsa.versus.restlet.adapter;

import java.util.HashSet;
import java.util.Set;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 * Multiple adapters.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class AdaptersServerResource extends ServerResource {

    public static final String URL = "/adapters";

    public static final String PATH_TEMPLATE = URL;

    @Get
    public HashSet<AdapterDescriptor> retrieve() {
        return ((ServerApplication) getApplication()).getAdapters();
    }

    @Get("html")
    public Representation asHtml() {
        ServerApplication server = (ServerApplication) getApplication();
        Set<AdapterDescriptor> adapters = server.getAdapters();
        if (adapters.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No adapters", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL;
            StringBuilder sb = new StringBuilder("<h3>Versus > Adapters</h3>"
                    + "<ul>");
            for (AdapterDescriptor adapter : adapters) {
                sb.append("<li><a href='").append(href);
                sb.append(adapter.getType()).append("'>");
                sb.append(adapter.getType()).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
