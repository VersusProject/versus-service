/**
 *
 */
package edu.illinois.ncsa.versus.restlet;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.adapter.AdaptersServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorsServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasuresServerResource;

/**
 * @author lmarini
 *
 */
public class VersusServerResource extends ServerResource {

    @Override
    @Get
    public Representation get() {
        String baseUrl = ((ServerApplication) getApplication()).getBaseUrl();
        StringBuilder content = new StringBuilder("<h3>Versus</h3>");
        content.append("<ul><li><a href='").
                append(baseUrl).append(AdaptersServerResource.URL).
                append("'>Adapters</a></li>");
        content.append("<li><a href='").
                append(baseUrl).append(ExtractorsServerResource.URL).
                append("'>Extractors</a></li>");
        content.append("<li><a href='").
                append(baseUrl).append(MeasuresServerResource.URL).
                append("'>Measures</a></li>");
        content.append("<li><a href='").
                append(baseUrl).append("/comparisons").
                append("'>Comparisons</a></li>");
        content.append("<li><a href='").
                append(baseUrl).append(SlavesServerResource.URL).
                append("'>Slaves</a></li>");
        content.append("<li><a href='").
                append(baseUrl).append("/distributions").
                append("'>Distributions</a></li>");
        content.append("<li><a href='").
                append(baseUrl).append("/decisionSupport").
                append("'>DecisionSupport</a></li></ul>");
        Representation representation = new StringRepresentation(content,
                MediaType.TEXT_HTML);
        return representation;
    }
}
