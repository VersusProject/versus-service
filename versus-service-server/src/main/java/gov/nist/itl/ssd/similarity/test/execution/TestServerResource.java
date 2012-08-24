package gov.nist.itl.ssd.similarity.test.execution;

import org.restlet.resource.Get;

import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import gov.nist.itl.ssd.similarity.test.Tool;

public class TestServerResource extends VersusServerResource {

	private static final Tool testingTool = new Tool();
	
		
	@Get("html")
	public String retrieve() {
		return testingTool.test("EuclideanL2Measure");
	}
}
