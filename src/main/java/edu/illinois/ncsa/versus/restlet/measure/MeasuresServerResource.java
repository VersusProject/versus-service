/**
 *
 */
package edu.illinois.ncsa.versus.restlet.measure;

import java.util.HashSet;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 * Multiple measures.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class MeasuresServerResource extends ServerResource {

    public static final String URL = "/measures";

    public static final String PATH_TEMPLATE = URL;

    @Get
    public HashSet<String> retrieve() {
        return ((ServerApplication) getApplication()).getMeasuresId();
    }

    @Get("html")
    public Representation asHtml() {
        ServerApplication server = (ServerApplication) getApplication();
        HashSet<String> measures = server.getMeasuresId();
        if (measures.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No measures", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL + '/';
            StringBuilder sb = new StringBuilder("<h3>Versus > Mesures</h3>"
                    + "<ul>");
            for (String measure : measures) {
                sb.append("<li><a href='").append(href);
                sb.append(measure).append("'>");
                sb.append(measure).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
