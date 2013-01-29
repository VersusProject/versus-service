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

import java.util.HashSet;
import java.util.Set;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.core.StringCollectionConverter;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import gov.nist.itl.ssd.sampling.SamplingRegistry;

/**
 *
 * @author antoinev
 */
public class IndividualsServerResource extends VersusServerResource {

    public static final String URL = "/individuals";

    public static final String PATH_TEMPLATE = URL;

    @Get
    public HashSet<String> retrieve() {
        SamplingRegistry samplingRegistry =
                ((ServerApplication) getApplication()).getSamplingRegistry();
        return new HashSet(samplingRegistry.getAvailableIndividualsIds());
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
        xstream.alias("individuals", Set.class);
        xstream.registerConverter(
                new StringCollectionConverter<HashSet<String>>() {

                    @Override
                    protected String getNodeName() {
                        return "individual";
                    }

                    @Override
                    protected HashSet<String> getNewT() {
                        return new HashSet<String>();
                    }

                    @Override
                    public boolean canConvert(Class type) {
                        return HashSet.class.isAssignableFrom(type);
                    }
                });

        return xstream.toXML(retrieve());
    }

    @Get("html")
    public Representation asHtml() {
        ServerApplication server = (ServerApplication) getApplication();
        Set<String> individuals = retrieve();
        if (individuals.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No individuals", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL + '/';
            StringBuilder sb = new StringBuilder("<h3>Versus > Individuals</h3>"
                    + "<ul>");
            for (String individual : individuals) {
                sb.append("<li><a href='").append(href);
                sb.append(individual).append("'>");
                sb.append(individual).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
