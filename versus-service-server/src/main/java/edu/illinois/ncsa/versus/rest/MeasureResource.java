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

import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.util.Templates;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * @author Luigi Marini
 * 
 */
@Path("/measures")
public class MeasureResource {

	private static Log log = LogFactory.getLog(MeasureResource.class);

	@GET
	@Produces("text/html")
	public String listHTML(@Context ServletContext context) {

		log.trace("/measures requested");
		Collection<Measure> measures = getMeasures(context);
		if (measures.size() == 0) {
			return "No measures";
		} else {
			String content = new String("<h3>Versus > Measures</h3><ul>");
			for (Measure measure : measures) {
				content += "<li><a href='measures/"
						+ measure.getClass().getName() + "'>"
						+ measure.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	@GET
	@Produces("application/json")
	public List<Map<String, Object>> listJSON(@Context ServletContext context) {

		log.trace("/measures requested");
		Collection<Measure> measures = getMeasures(context);
		List<Map<String, Object>> jsonReturn = new ArrayList<Map<String, Object>>();
		if (measures.size() == 0) {
			return jsonReturn;
		} else {
			for (Measure measure : measures) {
				Map<String, Object> json = new HashMap<String, Object>();
				json.put("name", measure.getName());
				json.put("id", measure.getClass().toString());
				json.put("supportedFeaturesTypes", measure.supportedFeaturesTypes());
				jsonReturn.add(json);
			}
			return jsonReturn;
		}
	}

	@GET
	@Path("/{id}")
	@Produces("text/html")
	public String getMeasureHTML(@PathParam("id") String id,
			@Context ServletContext context) throws IOException,
			TemplateException {

		log.trace("/measures/" + id + " requested");
		Measure measure = getMeasure(context, id);
		if (measure != null) {
			Configuration freeMarker = (Configuration) context
					.getAttribute(Configuration.class.getName());
			return Templates.create(freeMarker, "measure.ftl", measure,
					"measure");
		} else {
			return "Measure not found";
		}
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Map<String, Object> getMeasureJSON(@PathParam("id") String id,
			@Context ServletContext context) {

		log.trace("/measures/" + id + " requested");
		Measure measure = getMeasure(context, id);
		Map<String, Object> json = new HashMap<String, Object>();
		if (measure != null) {
			json.put("name", measure.getName());
			json.put("id", measure.getClass().getName());
			json.put("supportedFeaturesTypes", measure.supportedFeaturesTypes());
		} else {
			json.put("Error", "Measure not found");
		}
		return json;
	}

	private Collection<Measure> getMeasures(ServletContext context) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		return registry.getAvailableMeasures();
	}

	private Measure getMeasure(ServletContext context, String id) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Measure> measures = registry.getAvailableMeasures();
		Measure measure = null;
		for (Measure a : measures) {
			if (a.getClass().getName().equals(id)) {
				measure = a;
			}
		}
		return measure;
	}

}
