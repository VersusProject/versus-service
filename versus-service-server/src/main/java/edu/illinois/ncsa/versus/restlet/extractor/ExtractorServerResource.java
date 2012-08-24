/**
 *
 */
package edu.illinois.ncsa.versus.restlet.extractor;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.core.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;

/**
 * Single extractor.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ExtractorServerResource extends VersusServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/extractors/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public ExtractorDescriptor retrieve() {
        String id = (String) getRequest().getAttributes().get("id");
        try {
            return ((ServerApplication) getApplication()).getExtractor(id);
        } catch (NotFoundException ex) {
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
        xstream.processAnnotations(ExtractorDescriptor.class);
        return xstream.toXML(retrieve());
    }

    @Get("html")
    public Representation asHtml() {
        String id = (String) getRequest().getAttributes().get("id");
        try {
            ExtractorDescriptor extractor =
                    ((ServerApplication) getApplication()).getExtractor(id);
            StringBuilder sb = new StringBuilder(128);
            sb.append("Name: ").append(extractor.getName()).append("<br>");
            sb.append("Type: ").append(extractor.getType()).append("<br>");
            sb.append("Category: ").append(extractor.getCategory()).append("<br>");
            sb.append("Has help: ");
            if (extractor.hasHelp()) {
                sb.append("<a href=\"").append(id).append("/help\">").append(true).append("</a>");
            } else {
                sb.append(false);
            }
            sb.append("<br>");
            sb.append("Supported Adapters:<br>");
            for (String adapter : extractor.getSupportedAdapters()) {
                sb.append('\t').append(adapter).append("<br>");
            }
            sb.append("Supported Feature: ").append(
                    extractor.getSupportedFeature()).append("<br>");
            return new StringRepresentation(sb, MediaType.TEXT_HTML);
        } catch (NotFoundException e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Extractor " + id + " not found",
                    MediaType.TEXT_HTML);
        }
    }
}
