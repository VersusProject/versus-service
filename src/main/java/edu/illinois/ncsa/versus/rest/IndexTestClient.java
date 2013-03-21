package edu.illinois.ncsa.versus.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class IndexTestClient {

	private static Log log = LogFactory.getLog(SimpleComparisonTestClient.class);
	
	public static void main(String args[]){
	
	HttpClient client = new DefaultHttpClient();
	String id="85d8fd98-18fe-4352-9179-b2f1f3df73a6";
	//String requestUrl = "http://localhost:8080/api/v1/index/"+id+"/add";
	String requestUrl = "http://localhost:8080/api/v1/index/"+id+"/query";
	HttpPost httpPost = new HttpPost(requestUrl);
	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	nvps.add(new BasicNameValuePair("infile","http://www.ncsa.illinois.edu/includes/images/acb.jpg"));
	try {
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
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
       
		
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//log.debug(response);
	}
	}
