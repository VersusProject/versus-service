package gov.nist.itl.ssd.similarity.test.execution;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import gov.nist.itl.ssd.similarity.test.Tool;

public class TestServerResource extends VersusServerResource {

    private static final Tool testingTool = new Tool();

    @Get
    public Representation retrieve() {
        String file = testingTool.test("EuclideanL2Measure");
        Reference reference = getReference();
        String refString = reference.toString();
        String url = refString.substring(0, refString.lastIndexOf(reference.getLastSegment().toString()));
        redirectTemporary(url + "testsresult/" + file);
        return new StringRepresentation("Redirecting to html help.", MediaType.TEXT_PLAIN);
    }
}
