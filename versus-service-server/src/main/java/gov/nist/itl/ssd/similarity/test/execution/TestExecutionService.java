package gov.nist.itl.ssd.similarity.test.execution;

import gov.nist.itl.ssd.similarity.test.Tool;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("measures")
public class TestExecutionService 
{
	protected Tool testingTool;
	
	public TestExecutionService()
	{ 
			super();
			testingTool = new Tool();
	}
	
	// Stores state simply in a static collection class.
	   private static Map<String, String> measures = new TreeMap<String, String>();

	   @Path("/{measure}")
	   @PUT
	   @Produces("text/html")
	   public String create(@PathParam("measure") String measure,
	                                    @QueryParam("measure_name") String measureName)
	   {
	      measures.put(measure, measureName);
	      return "Added measure #" + measure;
	   }
	   
	   @Path("{command}")
	   @GET 
	   @Produces("text/html")
	   public String test( String command ) {
		   return testingTool.test( command );	// should be: testAllMeasures |  <measureName>
	   }

	   @Path("/{measure}")
	   @GET
	   @Produces("text/html")
	   public String testMeasure(@PathParam("measure") String measure )
	   {
	      if (measures.containsKey(measure))
	         return "<h2>Test Results on Measure " + measure +
	                    "</h2><p>Measure name: " + measures.get(measure);

	      throw new WebApplicationException(Response.Status.NOT_FOUND);
	   }

	   @Path("/testAllMeasures")
	   @GET
	   @Produces("text/html")
	   public String list() {
	      String header = "<h2>Test All Measures</h2>\n";

	      header += "<ul>";
	      for (Map.Entry<String, String> measure : measures.entrySet())
	         header += "\n<li>#" + measure.getKey() + " for " + measure.getValue() + "</li>";
	      header += "\n</ul>";
	      return header;
	   }
	}

