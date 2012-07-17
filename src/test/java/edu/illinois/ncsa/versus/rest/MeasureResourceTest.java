/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.service.JettyServer;

/**
 * Test measures resource.
 * 
 * @author Luigi Marini
 * 
 */
public class MeasureResourceTest {

	private static Log log = LogFactory.getLog(MeasureResourceTest.class);
	private static Server server;

	@BeforeClass
	public static void before() throws Exception {
		server = JettyServer.start(8080);
		log.info("Jetty started");
	}

	@Test
	public void testSubmit() throws ClientProtocolException, IOException {
		String requestUrl = "http://localhost:8080/versus/api/v1/measures";
		HttpGet httpGet = new HttpGet(requestUrl);
		// httpGet.addHeader("Accept:", "application/json");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		log.debug(httpGet.getRequestLine());
		HttpClient httpclient = new DefaultHttpClient();
		String responseStr = httpclient.execute(httpGet, responseHandler);
		log.debug("Response: " + responseStr);
	}

	@AfterClass
	public static void after() throws Exception {
		server.stop();
		log.debug("Jetty stopped");
	}
}
