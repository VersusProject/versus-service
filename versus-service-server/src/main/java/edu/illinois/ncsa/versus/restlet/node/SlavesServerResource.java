/**
 *
 */
package edu.illinois.ncsa.versus.restlet.node;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.core.StringCollectionConverter;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;

/**
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class SlavesServerResource extends VersusServerResource {

    public static final String URL = "/slaves";

    public static final String PATH_TEMPLATE = URL;

    @Get()
    public HashSet<String> retrieve() {
        return ((ServerApplication) getApplication()).getSlavesManager().getSlavesUrl();
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
        xstream.alias("slaves", Set.class);
        xstream.registerConverter(new StringCollectionConverter<HashSet<String>>() {

            @Override
            protected String getNodeName() {
                return "slave";
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

    /**
     * Get list of slaves known to this node as html.
     *
     * @return
     */
    @Get("html")
    public Representation asHtml() {
        Collection<String> slaves = retrieve();
        if (slaves.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No slaves", MediaType.TEXT_HTML);
            return representation;
        } else {
            StringBuilder content = new StringBuilder("<h3>Versus > Slaves</h3>"
                    + "<ul>");
            for (String slave : slaves) {
                content.append("<li><a href='").append(slave).
                        append("'>").append(slave).append("</a></li>");
            }
            content.append("</ul>");
            Representation representation = new StringRepresentation(content,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }

    /**
     * Register with this node as a slave.
     *
     * @param entity
     */
    @Post()
    public void submit(Representation entity) {
        Form form = new Form(entity);
        String url = form.getFirstValue("url");
        getLogger().log(Level.INFO, "registering slave {0}", url);
        ((ServerApplication) getApplication()).getSlavesManager().addSlave(url);
        setStatus(Status.SUCCESS_CREATED);
        getLogger().log(Level.INFO, "Slave registered: {0}", url);
    }
}
