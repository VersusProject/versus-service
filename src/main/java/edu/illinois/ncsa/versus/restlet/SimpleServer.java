package edu.illinois.ncsa.versus.restlet;

import java.net.URL;
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

    private static String baseUrl = "/versus/api";

    private static final Logger logger = Logger.getLogger(
            SimpleServer.class.getName());

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE,
                        "First argument must be the port number.", e);
                System.exit(1);
            }
        }
        if (args.length > 1) {
            master = args[1];
            logger.log(Level.INFO,
                    "The following master was specified on the command line: {0}",
                    master);
        }

        Component component = new Component();
        component.getClients().add(Protocol.HTTP);
        component.getDefaultHost().attach(baseUrl,
                new ServerApplication(port, baseUrl, master));

        //create embedding jetty server
        Server embedingJettyServer = new Server(
                component.getContext().createChildContext(),
                Protocol.HTTP,
                port,
                component);
        //construct and start JettyServerHelper
        JettyServerHelper jettyServerHelper = new HttpServerHelper(embedingJettyServer);
        jettyServerHelper.start();

        // HACK get rid of first 500 message
        try {
            new URL("http://localhost:" + port + baseUrl + "/measures").openStream().close();
        } catch (Exception e) {
            logger.log(Level.INFO, null, e);
        }
    }
}
