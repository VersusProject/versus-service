/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.util.Templates;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Extractors resource.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/extractors")
public class ExtractorResource {

	private static Log log = LogFactory.getLog(ExtractorResource.class);

	@GET
	@Path("/")
	@Produces("application/json")
	public List<Map<String, Object>> listJSON(@Context ServletContext context) {

		log.trace("/extractors requested");
		Collection<Extractor> extractors = getExtractors(context);
		List<Map<String, Object>> jsonReturn = new ArrayList<Map<String, Object>>();
		if (extractors.size() == 0) {
			return jsonReturn;
		} else {
			for (Extractor extractor : extractors) {
				Map<String, Object> json = new HashMap<String, Object>();
				json.put("name", extractor.getName());
				json.put("id", extractor.getClass().toString());
				json.put("supportedFeature", extractor.getFeatureType()
						.getName());
				jsonReturn.add(json);
			}
			return jsonReturn;
		}
	}

	@GET
	@Path("/")
	@Produces("text/html")
	public String listHTML(@Context ServletContext context) {

		log.trace("/extractors requested");
		Collection<Extractor> extractors = getExtractors(context);
		if (extractors.size() == 0) {
			return "No Extractors";
		} else {
			String content = new String("<h3>Versus > Extractors</h3><ul>");
			for (Extractor extractor : extractors) {
				content += "<li><a href='extractors/"
						+ extractor.getClass().getName() + "'>"
						+ extractor.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	/*@GET
	@Path("/{id}")
	@Produces("text/html")
	public String getExtractorHTML(@PathParam("id") String id,
			@Context ServletContext context) throws IOException,
			TemplateException {

		log.trace("/extractors/" + id + " requested");
		Extractor extractor = getExtractor(context, id);
		if (extractor != null) {
			Configuration freeMarker = (Configuration) context
					.getAttribute(Configuration.class.getName());
			return Templates.create(freeMarker, "extractor.ftl", extractor,
					"extractor");
		} else {
			return "Extractor not found";
		}
	}*/

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Map<String, Object> getExtractorJSON(@PathParam("id") String id,
			@Context ServletContext context) {

		log.trace("/extractors/" + id + " requested");
		Extractor extractor = getExtractor(context, id);
		Map<String, Object> json = new HashMap<String, Object>();
		if (extractor != null) {
			json.put("name", extractor.getName());
			json.put("id", extractor.getClass().getName());
			json.put("supportedFeature", extractor.getFeatureType().getName());
		} else {
			json.put("Error", "Extractor not found");
		}
		return json;
	}

	private Collection<Extractor> getExtractors(ServletContext context) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		return registry.getAvailableExtractors();
	}

	private Extractor getExtractor(ServletContext context, String id) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Extractor> Extractors = registry.getAvailableExtractors();
		Extractor extractor = null;
		for (Extractor e : Extractors) {
			if (e.getClass().getName().equals(id)) {
				extractor = e;
			}
		}
		return extractor;
	}

}
