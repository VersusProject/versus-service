/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Embedded jetty server.
 * 
 * @author Luigi Marini
 * 
 */
public class JettyServer {

	/**
	 * Start a jetty server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server(8080);

		WebAppContext context = new WebAppContext();
		context.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
		context.setResourceBase("./src/main/webapp");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		server.setHandler(context);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			System.out.println("Error starting jetty " + e);
		}

	}

}
