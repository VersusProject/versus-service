/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class SlavesServerResource extends ServerResource {

	/**
	 * Get list of slaves known to this node.
	 * 
	 * @return
	 */
	@Get
	public Representation list() {
		List<Slave> slaves = ((ServerApplication) getApplication()).getSlaves();
		if (slaves.size() == 0) {
			Representation representation = new StringRepresentation(
					"No slaves", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Slaves</h3>" + "<ul>");
			for (Slave slave : slaves) {
				content += "<li><a href='/versus/slaves/" + slave.getUrl()
						+ "'>" + slave.getUrl() + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,
					MediaType.TEXT_HTML);
			return representation;
		}
	}

	/**
	 * Register with this node as a slave.
	 * 
	 * @param entity
	 */
	@Post
	public void submit(Representation entity) {
		getLogger().info("Slave registration from " + getRequest().getResourceRef().getIdentifier());
		Form form = new Form(entity);
		String url = form.getFirstValue("url");
		getLogger().info("Slave registered: " + url);
		((ServerApplication) getApplication()).addSlave(url);
		setStatus(Status.SUCCESS_CREATED);
	}
}
