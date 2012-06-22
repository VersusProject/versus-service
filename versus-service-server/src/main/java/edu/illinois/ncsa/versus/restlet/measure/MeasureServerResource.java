/**
 *
 */
package edu.illinois.ncsa.versus.restlet.measure;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.core.measure.MeasureDescriptor;
import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;

/**
 * Single measure.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class MeasureServerResource extends VersusServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/measures/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public MeasureDescriptor retrieve() {
        String id = (String) getRequest().getAttributes().get("id");
        try {
            return ((ServerApplication) getApplication()).getMeasure(id);
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
        xstream.processAnnotations(MeasureDescriptor.class);
        return xstream.toXML(retrieve());
    }

    @Get("html")
    public Representation asHtml() {
        String id = (String) getRequest().getAttributes().get("id");
        try {
            MeasureDescriptor measure =
                    ((ServerApplication) getApplication()).getMeasure(id);
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(measure.getName()).append("<br>");
            sb.append("Type: ").append(measure.getType()).append("<br>");
            sb.append("Supported Features:<br>");
            for (String feature : measure.getSupportedFeatures()) {
                sb.append('\t').append(feature).append("<br>");
            }
            return new StringRepresentation(sb, MediaType.TEXT_HTML);
        } catch (NotFoundException e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Measure " + id + " not found",
                    MediaType.TEXT_PLAIN);
        }
    }
}
