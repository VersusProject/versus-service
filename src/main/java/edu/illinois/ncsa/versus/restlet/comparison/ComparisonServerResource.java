package edu.illinois.ncsa.versus.restlet.comparison;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.Slave;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Single comparison.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ComparisonServerResource extends ServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/comparisons/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public Comparison retrieve() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);

        // Guice storage
        Injector injector = Guice.createInjector(new RepositoryModule());
        ComparisonServiceImpl comparisonService =
                injector.getInstance(ComparisonServiceImpl.class);
        Comparison comparison = comparisonService.getComparison(id);

        if (comparison.getSlave() != null) {
            ComparisonStatus status = comparison.getStatus();
            if (status != ComparisonStatus.DONE
                    && status != ComparisonStatus.FAILED
                    && status != ComparisonStatus.ABORTED) {
                ServerApplication server = (ServerApplication)getApplication();
                Slave slave = server.getSlavesManager().getSlave(comparison.getSlave());
                comparison = slave.getComparison(comparison);
                comparisonService.updateValue(comparison.getId(), comparison.getValue());
                comparisonService.setStatus(comparison.getId(), comparison.getStatus());
            }
        }
        return comparison;
    }

    @Get("html")
    public Representation asHtml() {
        Comparison comparison = retrieve();
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Comparison ").append(comparison.getId()).
                append("</h3><br><br>");
        sb.append("Id: ").append(comparison.getId()).append("<br>");
        sb.append("First dataset: ").append(comparison.getFirstDataset()).
                append("<br>");
        sb.append("Second dataset: ").append(comparison.getSecondDataset()).
                append("<br>");
        sb.append("Adapter: ").append(comparison.getAdapterId()).append("<br>");
        sb.append("Extractor: ").append(comparison.getExtractorId()).
                append("<br>");
        sb.append("Measure: ").append(comparison.getMeasureId()).append("<br>");
        sb.append("Value: ").append(comparison.getValue()).append("<br>");
        sb.append("Status: ").append(comparison.getStatus()).append("<br>");
        sb.append("Slave: ");
        String slave = comparison.getSlave();
        if (slave != null) {
            sb.append("<a href='").append(slave).append("'>").
                    append(slave).append("</a>");
        } else {
            sb.append("none");
        }

        return new StringRepresentation(sb, MediaType.TEXT_HTML);
    }
}
