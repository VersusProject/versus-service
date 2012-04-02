/**
 *
 */
package edu.illinois.ncsa.versus.restlet.extractor;

import java.util.Collection;
import java.util.HashSet;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 * Multiple extractors.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ExtractorsServerResource extends ServerResource {

    public static final String URL = "/extractors";

    public static final String PATH_TEMPLATE = URL;

    @Get
    public HashSet<ExtractorDescriptor> retrieve() {
        return ((ServerApplication) getApplication()).getExtractors();
    }

    @Get("html")
    public Representation asHtml() {
        ServerApplication server = (ServerApplication) getApplication();
        Collection<ExtractorDescriptor> extractors = server.getExtractors();
        if (extractors.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No extractors", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL + '/';
            StringBuilder sb = new StringBuilder("<h3>Versus > Extractors</h3>"
                    + "<ul>");
            for (ExtractorDescriptor extractor : extractors) {
                sb.append("<li><a href='").append(href);
                sb.append(extractor.getType()).append("'>");
                sb.append(extractor.getType()).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
