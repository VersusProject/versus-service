/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

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
	public String list(@Context ServletContext context) {

		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Adapter> adapters = registry.getAvailableAdapters();

		if (adapters.size() == 0) {
			return "No adapters";
		} else {
			String content = new String("<h3>Versus > Adapters</h3><ul>");
			for (Adapter adapter : adapters) {
				content += "<li><a href='/versus/api/adapters/"
						+ adapter.getClass().getName() + "'>"
						+ adapter.getClass().getName() + "</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	@GET
	@Path("/{id}")
	@Produces("text/html")
	public String getAdapterHTML(@PathParam("id") String id,
			@Context ServletContext context) {
		log.trace("/adapters/" + id + " requested");
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Adapter> adapters = registry.getAvailableAdapters();
		Adapter adapter = null;
		for (Adapter a : adapters) {
			if (a.getClass().getName().equals(id)) {
				adapter = a;
			}
		}

		if (adapter != null) {
			String content = new String("<h3>Versus > Adapters > "
					+ adapter.getClass().getName() + "</h3><ul>")
					+ "<li>"
					+ adapter.getName()
					+ "</li>"
					+ "<li>"
					+ adapter.getClass().getName()
					+ "</li>"
					+ "<li>"
					+ adapter.getSupportedMediaTypes().toString()
					+ "</li>"
					+ adapter.getClass().getName() + "</li>" + "</ul>";
			return content;
		} else {
			return "Adapter not found";
		}
	}

	@GET
	@Produces("application/json")
	public List<Map<String, Object>> listJSON(@Context ServletContext context) {

		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Adapter> adapters = registry.getAvailableAdapters();
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

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Map<String, Object> getAdapterJSON(@PathParam("id") String id,
			@Context ServletContext context) {
		log.trace("/adapters/" + id + " requested");
		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Adapter> adapters = registry.getAvailableAdapters();
		Adapter adapter = null;
		for (Adapter a : adapters) {
			if (a.getClass().getName().equals(id)) {
				adapter = a;
			}
		}
		Map<String, Object> json = new HashMap<String, Object>();
		if (adapter != null) {
			json.put("name", adapter.getName());
			json.put("id", adapter.getClass().toString());
			json.put("supportedMimeTypes", adapter.getSupportedMediaTypes());
		} else {
			json.put("Error", "Adapter not found");
		}
		return json;
	}
}
