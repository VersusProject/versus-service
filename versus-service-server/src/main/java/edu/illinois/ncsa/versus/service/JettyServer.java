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
	 * Start a jetty server on port 8080.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			start(8080);
		} catch (Exception e) {
			System.out.println("Error starting jetty " + e);
		}
	}

	/**
	 * Start a jetty server.
	 * 
	 * @param args
	 */
	public static Server start(int port) throws Exception {
		Server server = new Server(port);
		WebAppContext context = new WebAppContext();
		context.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
		context.setResourceBase("./src/main/webapp");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		server.setHandler(context);
		server.start();
		return server;
	}
}
