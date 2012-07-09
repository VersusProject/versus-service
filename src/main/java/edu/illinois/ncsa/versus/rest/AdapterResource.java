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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

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

	@GET
	@Produces("text/html")
	public String list(@Context ServletContext context) {

		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());

		Collection<Adapter> adapters = registry.getAvailableAdapters();

		if (adapters.size() == 0) {
			return "No adapters";
		} else {
			String content = new String("<h3>Versus > Adapters</h3>" + "<ul>");
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
}
