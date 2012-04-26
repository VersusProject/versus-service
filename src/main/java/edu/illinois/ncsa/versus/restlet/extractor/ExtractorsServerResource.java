/**
 *
 */
package edu.illinois.ncsa.versus.restlet.extractor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.StringCollectionConverter;

/**
 * Multiple extractors.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ExtractorsServerResource extends ServerResource {

    public static final String URL = "/extractors";

    public static final String PATH_TEMPLATE = URL;

    @Get
    public HashSet<String> retrieve() {
        return ((ServerApplication) getApplication()).getExtractorsId();
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
        xstream.alias("extractors", Set.class);
        xstream.registerConverter(new StringCollectionConverter<HashSet<String>>() {

            @Override
            protected String getNodeName() {
                return "extractor";
            }

            @Override
            protected HashSet<String> getNewT() {
                return new HashSet();
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
        Collection<String> extractors = server.getExtractorsId();
        if (extractors.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No extractors", MediaType.TEXT_HTML);
            return representation;
        } else {
            String href = server.getBaseUrl() + URL + '/';
            StringBuilder sb = new StringBuilder("<h3>Versus > Extractors</h3>"
                    + "<ul>");
            for (String extractor : extractors) {
                sb.append("<li><a href='").append(href);
                sb.append(extractor).append("'>");
                sb.append(extractor).append("</a></li>");
            }
            sb.append("</ul>");
            Representation representation = new StringRepresentation(sb,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }
}
