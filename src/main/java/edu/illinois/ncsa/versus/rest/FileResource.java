/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 * File upload/download.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/files")
public class FileResource {

	private static final int IN_MEMORY_SIZE_THRESHOLD = 5000000;
	private static final int MAX_FILE_SIZE = 50000000;
	private static Log log = LogFactory.getLog(FileResource.class);

	@GET
	@Path("/{id}")
	@Produces("multipart/form-data")
	public String get(@PathParam("id") String id,
			@Context ServletContext context, @Context HttpServletResponse res)
			throws IOException {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);

		InputStream source = comparisonService.getFile(id);
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + id);
		ServletOutputStream out = res.getOutputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = source.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		source.close();
		return id;
	}

	@GET
	@Path("/upload")
	@Produces("text/html")
	public String getPage() {
		return "<form action='' enctype='multipart/form-data' method='post'>"
				+ "<input type='file' name='fileToUpload' size='40'>"
				+ "<input type='submit' name='submit' value='Send'>"
				+ "</form>";
	}

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response upload(@Context HttpServletRequest req,
			@Context ServletContext context) throws IOException {

		if (ServletFileUpload.isMultipartContent(req)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(IN_MEMORY_SIZE_THRESHOLD);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(MAX_FILE_SIZE);
			Injector injector = (Injector) context.getAttribute(Injector.class
					.getName());
			ComparisonServiceImpl comparisonService = injector
					.getInstance(ComparisonServiceImpl.class);

			try {
				String id = "";
				FileItemIterator iter = upload.getItemIterator(req);
				while (iter.hasNext()) {
					log.debug("Part found");
					FileItemStream item = iter.next();
					if (item.isFormField()) {
						log.debug("Found form field during file uplaod" + item);
					} else {
						String filename = item.getName();
						log.debug("File name: " + filename);
						try {
							InputStream in = item.openStream();
							id = comparisonService.addFile(in, filename);
							log.debug("File uploaded: " + id);
							return Response.status(200).entity(id).build();
						} catch (IOException e) {
							log.error("Error uploading file to server", e);
							return Response.status(500)
									.entity("Error uploading file").build();
						}
					}
				}
				log.error("Error uploading file. File not found.");
				return Response.status(500)
						.entity("Error uploading file. File not found.")
						.build();
			} catch (FileUploadException e) {
				log.error("Uploading file failed", e);
				return Response.status(500).entity("Error uploading file")
						.build();
			}
		} else {
			log.error("Upload is not multipart");
			return Response.status(500)
					.entity("Error uploading file. Upload is not multipart.")
					.build();
		}
	}
}
