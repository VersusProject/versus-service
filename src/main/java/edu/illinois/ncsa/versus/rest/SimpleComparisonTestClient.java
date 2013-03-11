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

public class SimpleComparisonTestClient {
	private static Log log = LogFactory.getLog(SimpleComparisonTestClient.class);
	
	public static void main(String args[]){
	
	HttpClient client = new DefaultHttpClient();
	String requestUrl = "http://localhost:8080/api/v1/comparisons";
	HttpPost httpPost = new HttpPost(requestUrl);
	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	nvps.add(new BasicNameValuePair("dataset1","http://www.cse.iitd.ac.in/~gayathri/ga2.jpg"));
	nvps.add(new BasicNameValuePair("dataset2","http://www.cse.iitd.ac.in/~chinmay/Chinmay.jpg"));
	//nvps.add(new BasicNameValuePair("adapter",
	//		"edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter"));
	nvps.add(new BasicNameValuePair("adapter",
				"edu.illinois.ncsa.versus.adapter.impl.BufferedImageAdapter"));
	nvps.add(new BasicNameValuePair("extractor",
			"edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor"));
	nvps.add(new BasicNameValuePair("measure",
			"edu.illinois.ncsa.versus.measure.impl.HistogramDistanceMeasure"));
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
        //idSlaveobject.setId(output);
        //compList.add(idSlaveobject);
         if(output1.equals("Modules not Supported by any Slaves")){
        	 
         }
         else{ 	 
        log.debug("Comparison ID: "+output1+"  Working Server URL"+requestUrl);
        
       String reqUrl=requestUrl+"/"+output1+"/value";
		
		log.debug("Getting Value: "+reqUrl);
						
		HttpGet httpGet=new HttpGet(reqUrl);
		//HttpResponse response1;
		
			
			response = client.execute(httpGet);
			
			BufferedReader br1 = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

	         
	         System.out.println("Response from Server ....GET: \n");
	       
	         while ((output = br1.readLine()) != null) {
	        	 output1=output;
	        	 log.debug("Response from GET value:"+output);
	           }
	         
	         /*ObjectMapper mapper = new ObjectMapper();
              Map<String,Object> json=new HashMap<String,Object>();
			 
              try {
					json = mapper.readValue(output1,new TypeReference<Map<String,Object>>() {});
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	                        
			//log.debug("Value="+json.get("value"));
		
	}} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//log.debug(response);
	}
	}

