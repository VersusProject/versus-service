package edu.illinois.ncsa.versus.restlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.ext.jetty.HttpServerHelper;
import org.restlet.ext.jetty.JettyServerHelper;

/**
 * Main restlet server.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class SimpleServer {

	private static int port = 8182;
	private static String master;
	private static Logger logger = Logger
			.getLogger("edu.illinois.ncsa.versus.restlet.SimpleServer");

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				logger.log(Level.SEVERE,
						"First argument must be the port number. "
								+ "Using default port number " + getPort(), e);
				System.exit(1);
			}
		}
		if (args.length > 1) {
			master = args[1];
			logger.log(Level.INFO, "The following master was specified on the command line: " + master);
		}
		Component component = new Component();
//		Server add = component.getServers().add(Protocol.HTTP, getPort());
//		add.getContext().getParameters().add("soLingerTime", "500");
		component.getClients().add(Protocol.HTTP);
		component.getDefaultHost().attach("/versus", new ServerApplication(getPort()));
//		component.start();
		
		Server embedingJettyServer=new Server(
                component.getContext(),
                Protocol.HTTP,
                getPort(),
                component
            );
		
        JettyServerHelper jettyServerHelper=new HttpServerHelper(embedingJettyServer);
        jettyServerHelper.start();


		
	}

	public static String getMaster() {
		return master;
	}

	public static int getPort() {
		return port;
	}
}
