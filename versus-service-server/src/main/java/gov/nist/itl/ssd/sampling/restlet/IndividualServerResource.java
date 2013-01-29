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

import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import edu.illinois.ncsa.versus.utility.HasCategory;
import edu.illinois.ncsa.versus.utility.HasHelp;
import gov.nist.itl.ssd.sampling.Individual;
import gov.nist.itl.ssd.sampling.IndividualDescriptor;
import gov.nist.itl.ssd.sampling.SamplingRegistry;

/**
 *
 * @author antoinev
 */
public class IndividualServerResource extends VersusServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/individuals/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public IndividualDescriptor retrieve() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        try {
            SamplingRegistry registry =
                    ((ServerApplication) getApplication()).getSamplingRegistry();
            Individual individual = registry.getIndividual(id);
            if (individual == null) {
                throw new NotFoundException("Individual " + id + " not found.");
            }
            return getDescriptor(individual);
        } catch (NotFoundException e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }
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
        xstream.processAnnotations(IndividualDescriptor.class);
        return xstream.toXML(retrieve());
    }

    @Get("html")
    public Representation asHtml() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        IndividualDescriptor individual = retrieve();
        if (individual == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Individual " + id + " not found",
                    MediaType.TEXT_HTML);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(individual.getName()).append("<br>");
        sb.append("Type: ").append(individual.getId()).append("<br>");
        sb.append("Category: ").append(individual.getCategory()).append("<br>");
        sb.append("Help: ").append(individual.hasHelp()).append("<br>");
        sb.append("Supported Media Types:<br>");
        for (String type : individual.getSupportedMediaTypes()) {
            sb.append('\t').append(type).append("<br>");
        }
        return new StringRepresentation(sb, MediaType.TEXT_HTML);
    }

    private IndividualDescriptor getDescriptor(Individual individual) {
        String category = individual instanceof HasCategory
                ? ((HasCategory) individual).getCategory() : "";
        boolean hasHelp = individual instanceof HasHelp;
        return new IndividualDescriptor(individual.getName(),
                individual.getClass().getName(), category,
                individual.getSupportedMediaTypes(), hasHelp);
    }
}
