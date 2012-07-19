/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package gov.nist.itl.ssd.sampling.restlet;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import edu.illinois.ncsa.versus.store.RepositoryModule;
import gov.nist.itl.ssd.sampling.Sampling;
import gov.nist.itl.ssd.versus.store.SamplingService;

/**
 *
 * @author antoinev
 */
public class SamplingServerResource extends VersusServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/samplings/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public Sampling retrieve() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);

        // Guice storage
        Injector injector = Guice.createInjector(new RepositoryModule());
        SamplingService samplingService =
                injector.getInstance(SamplingService.class);
        Sampling sampling = samplingService.getSampling(id);

        if (sampling == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }

//        if (sampling.getSlave() != null) {
//            ComparisonStatus status = comparison.getStatus();
//            if (status != ComparisonStatus.DONE
//                    && status != ComparisonStatus.FAILED
//                    && status != ComparisonStatus.ABORTED) {
//                ServerApplication server = (ServerApplication) getApplication();
//                Slave slave = server.getSlavesManager().getSlave(comparison.getSlave());
//                comparison = slave.getComparison(comparison);
//                comparisonService.updateValue(comparison.getId(), comparison.getValue());
//                comparisonService.setStatus(comparison.getId(), comparison.getStatus());
//                comparisonService.setError(comparison.getId(), comparison.getError());
//            }
//        }
        return sampling;
    }

    @Get("xml")
    public String asXml() {
        XStream xstream = new XStream();
        return fillAndConvert(xstream);
    }

    @Get("json")
    public String asJson() {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        return fillAndConvert(xstream);
    }

    private String fillAndConvert(XStream xstream) {
        xstream.processAnnotations(Sampling.class);
        return xstream.toXML(retrieve());
    }

    @Get("html")
    public Representation asHtml() {
        Sampling sampling = retrieve();
        if (sampling == null) {
            return new StringRepresentation("Sampling not found.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Sampling ").append(sampling.getId()).
                append("</h3><br><br>");
        sb.append("Id: ").append(sampling.getId()).append("<br>");
        sb.append("Individual: ").append(sampling.getIndividual()).append("<br>");
        sb.append("Sampler: ").append(sampling.getSampler()).append("<br>");
        sb.append("Sample size: ").append(sampling.getSampleSize()).append("<br>");
        sb.append("Datasets:<br>");
        for(String dataset : sampling.getDatasets()) {
            sb.append(dataset).append("<br>");
        }
        sb.append("<br>");
        sb.append("Sample:<br>");
        for(String dataset : sampling.getSample()) {
            sb.append(dataset).append("<br>");
        }
        sb.append("<br>");
        sb.append("Status: ").append(sampling.getStatus()).append("<br>");
//        sb.append("Error: ").append(sampling.getError()).append("<br>");
//        sb.append("Slave: ");
//        String slave = sampling.getSlave();
//        if (slave != null) {
//            sb.append("<a href='").append(slave).append("'>").
//                    append(slave).append("</a>");
//        } else {
//            sb.append("none");
//        }

        return new StringRepresentation(sb, MediaType.TEXT_HTML);
    }
}
