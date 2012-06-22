/**
 *
 */
package edu.illinois.ncsa.versus.restlet.measure;

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

/**
 * Multiple measures.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class MeasuresServerResource extends VersusServerResource {

    public static final String URL = "/measures";

    public static final String PATH_TEMPLATE = URL;

    @Get
    public HashSet<String> retrieve() {
        return ((ServerApplication) getApplication()).getMeasuresId();
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
        xstream.alias("measures", Set.class);
        xstream.registerConverter(new StringCollectionConverter<HashSet<String>>() {

            @Override
            protected String getNodeName() {
                return "measure";
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
        HashSet<String> measures = server.getMeasuresId();
        if (measures.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No measures", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL + '/';
            StringBuilder sb = new StringBuilder("<h3>Versus > Measures</h3>"
                    + "<ul>");
            for (String measure : measures) {
                sb.append("<li><a href='").append(href);
                sb.append(measure).append("'>");
                sb.append(measure).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
