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
    public HashSet<MeasureDescriptor> retrieve() {
        return ((ServerApplication) getApplication()).getMeasures();
    }

    @Get("html")
    public Representation asHtml() {
        ServerApplication server = (ServerApplication) getApplication();
        HashSet<MeasureDescriptor> measures = server.getMeasures();
        if (measures.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No measures", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL;
            StringBuilder sb = new StringBuilder("<h3>Versus > Mesures</h3>"
                    + "<ul>");
            for (MeasureDescriptor measure : measures) {
                sb.append("<li><a href='").append(href);
                sb.append(measure.getType()).append("'>");
                sb.append(measure.getType()).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
