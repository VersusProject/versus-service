/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Upload a file to the system. GET returns a simple html form to manually
 * upload a file.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class UploadServerResource extends ServerResource {

	@Get("html")
	public Representation getPage() {

		String text = "<form action='' enctype='multipart/form-data' method='post'>"
				+ "<input type='file' name='fileToUpload' size='40'>"
				+ "<input type='submit' value='Send'>" + "</form>";
		return new StringRepresentation(text, MediaType.TEXT_HTML);

	}

	@Post
	public Representation accept(Representation entity) throws Exception {
		Representation rep = null;
		if (entity != null) {
			if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
					true)) {

				// The Apache FileUpload project parses HTTP requests which
				// conform to RFC 1867, "Form-based File Upload in HTML". That
				// is, if an HTTP request is submitted using the POST method,
				// and with a content type of "multipart/form-data", then
				// FileUpload can parse that request, and get all uploaded files
				// as FileItem.

				// 1/ Create a factory for disk-based file items
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1000240);

				// 2/ Create a new file upload handler based on the Restlet
				// FileUpload extension that will parse Restlet requests and
				// generates FileItems.
				RestletFileUpload upload = new RestletFileUpload(factory);
				List<FileItem> items;

				// 3/ Request is parsed by the handler which generates a
				// list of FileItems
				items = upload.parseRequest(getRequest());

				// Process only the uploaded item called "fileToUpload" and
				// save it on disk
				boolean found = false;
				String id = "";
				for (final Iterator<FileItem> it = items.iterator(); it
						.hasNext() && !found;) {
					FileItem fi = it.next();
					if (fi.getFieldName().equals("fileToUpload")) {
						found = true;
						Injector injector = Guice
								.createInjector(new RepositoryModule());
						ComparisonServiceImpl comparisonService = injector
								.getInstance(ComparisonServiceImpl.class);
						id = comparisonService.addFile(fi.getInputStream());
					}
				}

				// Once handled, the content of the uploaded file is sent
				// back to the client.
				if (found) {
					// Create a new representation based on disk file.
					// The content is arbitrarily sent as plain text.
					rep = new StringRepresentation(id, MediaType.TEXT_PLAIN);
				} else {
					// Some problem occurs, sent back a simple line of text.
					rep = new StringRepresentation("File not uploaded",
							MediaType.TEXT_PLAIN);
				}
			}
		} else {
			// POST request with no entity.
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		}

		return rep;
	}
}
