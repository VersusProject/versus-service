/**
 *
 */
package edu.illinois.ncsa.versus.restlet.adapter;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.core.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;

/**
 * Single adapter.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class AdapterServerResource extends VersusServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String URL = "/adapters/";

    public static final String PATH_TEMPLATE = URL + '{' + ID_PARAMETER + '}';

    @Get
    public AdapterDescriptor retrieve() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        try {
            return ((ServerApplication) getApplication()).getAdapter(id);
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
        xstream.processAnnotations(AdapterDescriptor.class);
        return xstream.toXML(retrieve());
    }

    @Get("html")
    public Representation asHtml() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        AdapterDescriptor adapter;
        try {
            adapter = ((ServerApplication) getApplication()).getAdapter(id);
        } catch (NotFoundException e) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("Adapter " + id + " not found",
                    MediaType.TEXT_HTML);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(adapter.getName()).append("<br>");
        sb.append("Type: ").append(adapter.getId()).append("<br>");
        sb.append("Category: ").append(adapter.getCategory()).append("<br>");
        sb.append("Has help: ");
        if(adapter.hasHelp()) {
                sb.append("<a href=\"").append(id).append("/help\">").append(true).append("</a>");
        } else {
            sb.append(false);
        }
        sb.append("<br>");
        sb.append("Supported Media Types:<br>");
        for (String type : adapter.getSupportedMediaTypes()) {
            sb.append('\t').append(type).append("<br>");
        }
        return new StringRepresentation(sb, MediaType.TEXT_HTML);
    }
}
