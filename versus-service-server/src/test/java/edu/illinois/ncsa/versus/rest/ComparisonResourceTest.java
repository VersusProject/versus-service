/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.service.JettyServer;

//import edu.illinois.ncsa.versus.ds.web.JettyServer;

/**
 * Test compirison resource.
 * 
 * @author Luigi Marini
 * 
 */
public class ComparisonResourceTest {

	private static Log log = LogFactory.getLog(ComparisonResourceTest.class);
	private static Server server;

	@BeforeClass
	public static void before() throws Exception {
		server = JettyServer.start(8080);
		log.info("Jetty started");
	}

	@Test
	public void testSubmit() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		String requestUrl = "http://localhost:8080/api/v1/comparisons";
		HttpPost httpPost = new HttpPost(requestUrl);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("dataset1",
				"http://www.google.com/images/srpr/logo3w.png"));
		nvps.add(new BasicNameValuePair("dataset2",
				"http://l.yimg.com/a/i/mntl/ww/events/p.gif"));
		nvps.add(new BasicNameValuePair("adapter",
				"edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter"));
		nvps.add(new BasicNameValuePair("extractor",
				"edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor"));
		nvps.add(new BasicNameValuePair("measure",
				"edu.illinois.ncsa.versus.measure.impl.HistogramDistanceMeasure"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = client.execute(httpPost);
		log.debug(response);
	}

	@AfterClass
	public static void after() throws Exception {
		server.stop();
		log.debug("Jetty stopped");
	}
}
