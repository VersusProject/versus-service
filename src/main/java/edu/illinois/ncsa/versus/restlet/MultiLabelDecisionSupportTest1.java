package edu.illinois.ncsa.versus.restlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.eclipse.jetty.server.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;


import edu.illinois.ncsa.versus.rest.MultiLabelDecisionSupportResource;


import java.lang.String;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.test.BaseResourceTest;
import org.restlet.resource.ResourceException;
/**
 * Simple testing client.
 * 
 * @author Devin Bonnie
 * 
 */
public class MultiLabelDecisionSupportTest1 {

	private static Log log = LogFactory.getLog(MultiLabelDecisionSupportTest1.class);
	public static void main(String[] args) throws ResourceException,
			IOException {
		
		
		HttpClient client = new DefaultHttpClient();
		
		String requestUrl="http://localhost:8080/api/v1/multiLabelDecisionSupport";
		
		HttpPost httpPost = new HttpPost(requestUrl);
		
		//try {
			ArrayList<String> s1 = new ArrayList<String>();
			ArrayList<String> s2 = new ArrayList<String>();
			
			String adapter="edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter";
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						
			s1.add("http://www.ncsa.illinois.edu/includes/images/rss.jpg");
			s1.add("http://www.ncsa.illinois.edu/includes/images/2010.jpg");
			
			s2.add("http://www.ncsa.illinois.edu/includes/images/about.jpg");
			s2.add("http://www.ncsa.illinois.edu/includes/images/acb.jpg");
			
						
			String k ="3";
			nvps.add(new BasicNameValuePair("k",k));
			nvps.add(new BasicNameValuePair("adapter",adapter));
			//nvps.add(new BasicNameValuePair("method","probabilistic"));
			nvps.add(new BasicNameValuePair("method","inverseKmeans"));
			
			//nvps.add(new BasicNameValuePair("data[0].label","1"));
			nvps.add(new BasicNameValuePair("data0",s1.get(0)));
			
			//nvps.add(new BasicNameValuePair("data[1].label","1"));
			nvps.add(new BasicNameValuePair("data0",s1.get(1)));
			
			//nvps.add(new BasicNameValuePair("data[2].label","2"));
			nvps.add(new BasicNameValuePair("data1",s2.get(0)));
			
			//nvps.add(new BasicNameValuePair("data[3].label","2"));
			nvps.add(new BasicNameValuePair("data1",s2.get(1)));
			nvps.add(new BasicNameValuePair("data2","http://www.ncsa.illinois.edu/includes/images/bottomline.jpg"));
			nvps.add(new BasicNameValuePair("data2","http://www.ncsa.illinois.edu/includes/images/collab.jpg"));
			/*nvps.add(new BasicNameValuePair("data[0].label","1"));
			nvps.add(new BasicNameValuePair("data[0].url",s1.get(0)));
			
			nvps.add(new BasicNameValuePair("data[1].label","1"));
			nvps.add(new BasicNameValuePair("data[1].url",s1.get(1)));
			
			nvps.add(new BasicNameValuePair("data[2].label","2"));
			nvps.add(new BasicNameValuePair("data[2].url",s2.get(0)));
			
			nvps.add(new BasicNameValuePair("data[3].label","2"));
			nvps.add(new BasicNameValuePair("data[3].url",s2.get(1)));
			*/
			/*httpPost.addHeader("k",k);
			httpPost.addHeader("adapter",adapter);
			httpPost.addHeader("method","probabilistic");
			httpPost.addHeader("data[0].label","1");
			httpPost.addHeader("data[0].url",s1.get(0));
			
			httpPost.addHeader("data[1].label","1");
			httpPost.addHeader("data[1].url",s1.get(1));
			
			httpPost.addHeader("data[2].label","2");
			httpPost.addHeader("data[2].url",s2.get(0));
			
			httpPost.addHeader("data[3].label","2");
			httpPost.addHeader("data[3].url",s2.get(1));
			*/
			/*MockHttpResponse response = new MockHttpResponse();
			MockHttpRequest request;
			try {
				request = MockHttpRequest.post("MLDSForm").accept(MediaType.TEXT_PLAIN).contentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			request.addFormHeader("k",k);
			request.addFormHeader("adapter",adapter);
			request.addFormHeader("method","probabilistic");
			
			request.addFormHeader("data[0].label","1");
			request.addFormHeader("data[0].url",s1.get(0));
			
			request.addFormHeader("data[1].label","1");
			request.addFormHeader("data[1].url",s1.get(1));
			
			request.addFormHeader("data[2].label","2");
			request.addFormHeader("data[2].url",s2.get(0));
			
			request.addFormHeader("data[3].label","2");
			request.addFormHeader("data[3].url",s2.get(1));
			
			org.jboss.resteasy.core.Dispatcher dispatcher = MockDispatcherFactory.createDispatcher();
			((org.jboss.resteasy.core.Dispatcher) dispatcher).invoke(request, response);
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			
			
			//MultiLabelDecisionSupport mlds = new MultiLabelDecisionSupport(d, "edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter",k,"inverseKmeans");
			
			//dsResource.post(getMultiLabelDecisionSupportRepresentation(mlds));
			//httpPost.addFormHeader();
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				//httpPost.setEntity(new UrlEncodedFormEntity(HTTP.UTF_8));
				//httpPost   client.
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpResponse response=null;
			try {
				response = client.execute(httpPost);
				BufferedReader br = new BufferedReader(
		                new InputStreamReader((response.getEntity().getContent())));

		                String output,output1=null;
		               System.out.println("Response from the Server ....POST: \n");
		        
		               while ((output = br.readLine()) != null) {
		        	    output1=output;
			            log.debug(output);
		               }
		        /* if(output1.equals("Modules not Supported by any Slaves")){
		        	 
		         }
		         else{ 	 
		             log.debug("Comparison ID: "+output1+"  Working Server URL"+requestUrl);
		             }
		         } catch (ClientProtocolException e) {
		     		// TODO Auto-generated catch block
		     		e.printStackTrace();
		     	} 	catch (IOException e) {
		     		// TODO Auto-generated catch block
		     		e.printStackTrace();
		     	}*/
		} catch (ResourceException e) {
			log.error("Error connecting to server", e);
		}

	}
		
	
}
