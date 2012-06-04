/**
 *
 */
package edu.illinois.ncsa.versus.restlet.comparison;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 * @author lmarini
 *
 */
public class ComparisonValueServerResource extends ServerResource {

    @Get
    public Representation status() {
        String id = (String) getRequest().getAttributes().get("id");

        ComparisonServiceImpl comparisonService = ServerApplication.getInjector().
                getInstance(ComparisonServiceImpl.class);
        Comparison comparison = comparisonService.getComparison(id);
        String value = comparison.getValue();

        if (value != null) {
            return new StringRepresentation(value, MediaType.TEXT_HTML);
        } else {
            return new StringRepresentation("N/A", MediaType.TEXT_HTML);
        }
    }
}
