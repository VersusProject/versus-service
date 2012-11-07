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

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.util.Templates;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Adapters resource.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/adapters")
public class AdapterResource {

	private static Log log = LogFactory.getLog(AdapterResource.class);

	@GET
	@Produces("text/html")
	public String listHTML(@Context ServletContext context) {

		log.trace("/adapters requested");
		// Collection<Adapter> adapters = getAdapters(context);
		List<Adapter> adapters = CompareRegistry.getAdapters();
		if (adapters.size() == 0) {
			return "No adapters";
		} else {
			String content = new String("<h3>Versus > Adapters</h3><ul>");
			for (Adapter adapter : adapters) {
				content += "<li><a href='adapters/"
						+ adapter.getClass().getName() + "'>"
						+ adapter.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	@GET
	@Produces("application/json")
	public List<Map<String, Object>> listJSON(@Context ServletContext context) {

		log.trace("/adapters requested");
		Collection<Adapter> adapters = getAdapters(context);
		List<Map<String, Object>> jsonReturn = new ArrayList<Map<String, Object>>();
		if (adapters.size() == 0) {
			return jsonReturn;
		} else {
			for (Adapter a : adapters) {
				Map<String, Object> json = new HashMap<String, Object>();
				json.put("name", a.getName());
				json.put("id", a.getClass().toString());
				json.put("supportedMimeTypes", a.getSupportedMediaTypes());
				jsonReturn.add(json);
			}
			return jsonReturn;
		}
	}

	/*@GET
	@Path("/{id}")
	@Produces("text/html")
	public String getAdapterHTML(@PathParam("id") String id,
			@Context ServletContext context) throws IOException,
			TemplateException {

		log.debug("/adapters/" + id + " requested");
		Adapter adapter = getAdapter(context, id);
		if (adapter != null) {
			Configuration freeMarker = (Configuration) context
					.getAttribute(Configuration.class.getName());
			return Templates.create(freeMarker, "adapter.ftl", adapter,
					"adapter");
		} else {
			return "Adapter not found";
		}
	}*/

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Map<String, Object> getAdapterJSON(@PathParam("id") String id,
			@Context ServletContext context) {

		log.debug("/adapters/" + id + " requested");
		Adapter adapter = getAdapter(context, id);
		Map<String, Object> json = new HashMap<String, Object>();
		if (adapter != null) {
			json.put("name", adapter.getName());
			json.put("id", adapter.getClass().getName());
			json.put("supportedMimeTypes", adapter.getSupportedMediaTypes());
		} else {
			json.put("Error", "Adapter not found");
		}
		return json;
	}

	private Collection<Adapter> getAdapters(ServletContext context) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		return registry.getAvailableAdapters();
	}

	private Adapter getAdapter(ServletContext context, String id) {
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Adapter> adapters = registry.getAvailableAdapters();
		Adapter adapter = null;
		for (Adapter a : adapters) {
			if (a.getClass().getName().equals(id)) {
				adapter = a;
			}
		}
		return adapter;
	}
}
