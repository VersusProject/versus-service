/**
 * 
 */
package edu.illinois.ncsa.versus.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import org.eclipse.jetty.webapp.WebAppContext;


import edu.illinois.ncsa.versus.restlet.PropertiesUtil;

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
	private static Log log = LogFactory.getLog(JettyServer.class);
	
	static int port=8080;
	
	public static void main(String[] args) {
		String Master;
		
		try {
			
			if(args.length>0){
				port = Integer.parseInt(args[0]);
			}
			start(port);
			log.debug("Versus Server STARTED ...");
			
			if(args.length>1){
				Master=args[1];
				
			}
			else	
			{		
			
				Properties properties=PropertiesUtil.load();
				Master=properties.getProperty("master");
			}	
			
			String ownurl="http://"+InetAddress.getLocalHost().getHostAddress()+":"+port+"/api/v1";
			
			URL myURL=new URL(ownurl);
			URL masterURL=new URL(Master);
			
			log.debug("myURL= "+myURL+"masterURL"+masterURL);
			
			if(!masterURL.equals(myURL)){
				    log.debug("I am a SLAVE.");
				    log.debug("Registering with the Master");
			        registerWithMaster(masterURL);
			}
			 else
			       log.debug("I am the MASTER");
		} catch (Exception e) {
			  log.debug("Error starting jetty " + e);
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
	
	private static void registerWithMaster(URL masterURL) throws ClientProtocolException, IOException{
		String slaveurl;
		slaveurl="http://"+InetAddress.getLocalHost().getHostAddress()+":"+port+"/api/v1";
		HttpClient client = new DefaultHttpClient();
		//String requestUrl = "http://localhost:8080/api/v1/slaves/add";
		String requestUrl=masterURL.toString()+"/slaves/add";
		HttpPost httpPost = new HttpPost(requestUrl);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("url",slaveurl));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = client.execute(httpPost);
		log.debug(response);
	}
	

}



