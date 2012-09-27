/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Return the bytes of a specific file.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class FileServerResource extends VersusServerResource {

	@Get
	public Representation getFile() {
		final String id = (String) getRequest().getAttributes().get("id");
		OutputRepresentation ouRepresentation = new OutputRepresentation(
				MediaType.APPLICATION_ALL) {

			@Override
			public void write(OutputStream outputStream) throws IOException {
				Injector injector = Guice
						.createInjector(new RepositoryModule());
				ComparisonServiceImpl comparisonService = injector
						.getInstance(ComparisonServiceImpl.class);
				InputStream source;
				try {
					source = comparisonService.getFile(id);
					byte[] buf = new byte[1024];
					int len;
					while ((len = source.read(buf)) > 0) {
						outputStream.write(buf, 0, len);
					}
					source.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		return ouRepresentation;
	}
}
