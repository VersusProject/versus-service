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
    public HashSet<String> retrieve() {
        return ((ServerApplication) getApplication()).getAdaptersId();
    }

    @Get("html")
    public Representation asHtml() {
        ServerApplication server = (ServerApplication) getApplication();
        Set<String> adapters = server.getAdaptersId();
        if (adapters.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No adapters", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL + '/';
            StringBuilder sb = new StringBuilder("<h3>Versus > Adapters</h3>"
                    + "<ul>");
            for (String adapter : adapters) {
                sb.append("<li><a href='").append(href);
                sb.append(adapter).append("'>");
                sb.append(adapter).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
