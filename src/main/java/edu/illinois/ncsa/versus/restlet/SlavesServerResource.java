/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.Set;
import java.util.logging.Level;

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
		Set<Slave> slaves = ((ServerApplication) getApplication()).getSlaves();
		if (slaves.isEmpty()) {
			Representation representation = new StringRepresentation(
					"No slaves", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Slaves</h3>" + "<ul>");
			for (Slave slave : slaves) {
				content += "<li><a href='" + slave.getUrl() + "'>"
						+ slave.getUrl() + "</a></li>";
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
		getLogger().log(Level.INFO, "Slave registration from {0}",
                getRequest().getResourceRef().getIdentifier());
		Form form = new Form(entity);
		String url = form.getFirstValue("url");
		getLogger().log(Level.INFO, "Slave registered: {0}", url);
		((ServerApplication) getApplication()).addSlave(url);
		setStatus(Status.SUCCESS_CREATED);
	}
}
