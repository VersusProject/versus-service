package gov.nist.itl.ssd.similarity.test.execution;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import gov.nist.itl.ssd.similarity.test.Tool;

public class TestServerResource extends VersusServerResource {

    @Get
    public Representation retrieve() {
        String command = (String) getRequest().getAttributes().get("command");
        Reference reference = getReference();
        List<String> segments = reference.getSegments();
        segments.remove(segments.size() - 1);
        String file;
        if (command == null) {
            file = new Tool().test();
        } else {
            file = new Tool().test(command);
            segments.remove(segments.size() - 1);
        }
        String url = "http://" + reference.getAuthority() + '/' + StringUtils.join(segments, '/');
        redirectTemporary(url + "/testsresult/" + file);
        return new StringRepresentation("Redirecting to html help.", MediaType.TEXT_PLAIN);
    }
}
