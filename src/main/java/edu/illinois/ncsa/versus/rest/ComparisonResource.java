/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import org.jboss.resteasy.annotations.Form;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.ComparisonStatusHandler;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.Job;
import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.Comparison;
import edu.illinois.ncsa.versus.restlet.Slave;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 * Submit and query comparisons.
 * 
 * @author Luigi Marini
 * 
 */
@Path("/comparisons")
public class ComparisonResource {

	private static Log log = LogFactory.getLog(ComparisonResource.class);
	
	//ArrayList<HashIdSlave> compList=new ArrayList<HashIdSlave>();
	//HashMap<String,String> hashList=new HashMap<String,String>();

	@GET
	@Produces("application/json")
	public List<String> list(@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		log.debug("I am inside GET list ");
		System.out.println("I am inside GET list ");
		List<String> json = new ArrayList<String>();
		for (Comparison comparison : comparisonService.listAll()) {
			json.add(comparison.getId());
		}
		return json;
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getComparison(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		log.debug("I am inside GET getComparison ");
		System.out.println("I am inside GET getComparison ");
		
		Comparison c = comparisonService.getComparison(id);
		if (c == null) {
			log.debug("Comparison not found " + id);
			return Response.status(404)
					.entity("Comparison " + id + " not found.").build();
		} else {
			log.debug("Comparison found " + id);
			return Response.status(200).entity(c).build();
		}
	}

	@GET
	@Path("/{id}/status")
	@Produces("text/plain")
	public Response getStatus(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		
		log.debug("I am inside GET: /id/status ");
		
		//Map<String, Object> json = new HashMap<String, Object>();
		Response json;
		
		HashIdSlave compList= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
		
		log.debug("hash List size: "+compList.gethashList().size()+"  containsKey"+compList.gethashList().containsKey(id));
		
		if(compList.gethashList().containsKey(id)){
			
	       log.debug("Slave Which did this Comparison:Url: "+compList.gethashList().get(id));		
			
	       json=queryGetStatusSlaves(id,compList.gethashList().get(id),context);
			
		
	       	log.debug("Received Value from Slave Server: "+json);
		
					
		return json;
		}
		else{
			
	   	
		/*log.debug("Comparison status: "+c.getStatus());
		
		if( c.getStatus() != ComparisonStatus.DONE ){
			 // while(c.getStatus()!=ComparisonStatus.DONE){	
			    try {
					//Thread.sleep(5);
			    	synchronized(c){
			    	c.wait();
			    	}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return json;
		}*/
				
		Comparison c = comparisonService.getComparison(id);
		if (c == null) {
			log.debug("Comparison not found " + id);
			return Response.status(404)
					.entity("Comparison " + id + " not found.").build();
		} else {
			log.debug("Comparison found " + id);
			return Response.status(200).entity(c.getStatus()).build();
		}
		}
	}
	@GET
	@Path("/{id}/value")
	@Produces("text/plain")
	public Response getValue(@PathParam("id") String id,
			@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		
		log.debug("I am inside GET: /id/value ");
		
		//Map<String, Object> json = new HashMap<String, Object>();
		Response json;
		
		HashIdSlave compList= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
		
		log.debug("hash List size: "+compList.gethashList().size()+"  containsKey"+compList.gethashList().containsKey(id));
		
		if(compList.gethashList().containsKey(id)){
			
	       log.debug("Slave Which did this Comparison:Url: "+compList.gethashList().get(id));		
			
	       json=queryGetStatusSlaves(id,compList.gethashList().get(id),context);
			
		
	       	log.debug("Received Value from Slave Server: "+json);
		
					
		return json;
		}
		else{
			
	   	
		/*log.debug("Comparison status: "+c.getStatus());
		
		if( c.getStatus() != ComparisonStatus.DONE ){
			 // while(c.getStatus()!=ComparisonStatus.DONE){	
			    try {
					//Thread.sleep(5);
			    	synchronized(c){
			    	c.wait();
			    	}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return json;
		}*/
				
		Comparison c = comparisonService.getComparison(id);
		if (c == null) {
			log.debug("Comparison not found " + id);
			return Response.status(404)
					.entity("Comparison " + id + " not found.").build();
		} else {
			log.debug("Comparison found " + id);
			return Response.status(200).entity(c.getValue()).build();
		}
		}
	}
	
	
	
	
	@GET
	@Path("/{id}/value")
	@Produces("application/json")
	//@Produces("text/plain")
	//public Response getValue(@PathParam("id") String id,
		//	@Context ServletContext context) {
	public Map<String, Object> getJson(@PathParam("id") String id,
			@Context ServletContext context) {
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());

		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		
		log.debug("I am inside GET :/id/value:  ");
		
		
		Map<String, Object> json = new HashMap<String, Object>();
		
		HashIdSlave compList= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
		
		log.debug("hash List size: "+compList.gethashList().size()+"  containsKey"+compList.gethashList().containsKey(id));
		
		if(compList.gethashList().containsKey(id)){
			
	       log.debug("Slave Which did Comparison:Url: "+compList.gethashList().get(id));		
			
	       json=queryGetSlaves(id,compList.gethashList().get(id),context);
		
		
		
	       	log.debug("Received Value from Slave Server: "+json);
		
					
		return json;
		}
		else{
			
	    log.debug("ELSE: Comparison ID not in List: Then it is in my memory ");		
		Comparison c = comparisonService.getComparison(id);
		/*if (c == null) {
			log.debug("Comparison not found " + id);
			return Response.status(404)
					.entity("Comparison " + id + " not found.").build();
		} else {
			log.debug("Comparison found " + id);
			return Response.status(200).entity(c.getValue()).build();
		}*/
		//log.debug("getjson");
		
		
		
		log.debug("Comparison status: "+c.getStatus());
		
		if( c.getStatus() != ComparisonStatus.DONE ){
			 // while(c.getStatus()!=ComparisonStatus.DONE){	
			    try {
					//Thread.sleep(5);
			    	synchronized(c){
			    	c.wait();
			    	}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		//while(c.getStatus()!=ComparisonStatus.DONE){
		//}	
		
			log.debug("getjson");
			log.debug("c.getValue"+c.getValue());
			log.debug("status"+c.getStatus());
		    json.put("value",c.getValue());
		    json.put("status", c.getStatus());
		    json.put("other", c.getExtractorId());
		//}
		return json;
		}
	}

	/**
	 * Submit new comparison.
	 * 
	 * @param entity
	 * @return
	 */
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String submit(@Form ComparisonForm comparisonForm,
			@Context ServletContext context) {

		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());
		ExecutionEngine engine = (ExecutionEngine) context
				.getAttribute(ExecutionEngine.class.getName());
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		
     String master="";
     String slaveurl="";
	     
     SlavesList slaveList = (SlavesList) context.getAttribute(SlavesList.class.getName());
     List<Slave> slaves= slaveList.getSlaves();
                      //log.debug("SLAVE URL= "+slaves.get(0).getUrl());  
     if(slaves.size()==0){
    	 
        if (checkRequirements(registry, comparisonForm.adapter,
				comparisonForm.extractor, comparisonForm.measure)) {
			try {
                log.debug("I am SLAVE");
				final PairwiseComparison comparison = new PairwiseComparison();
				comparison.setId(UUID.randomUUID().toString());
				comparison.setFirstDataset(getFile(comparisonForm.dataset1));
				comparison.setSecondDataset(getFile(comparisonForm.dataset2));
				comparison.setAdapterId(comparisonForm.adapter);
				comparison.setExtractorId(comparisonForm.extractor);
				comparison.setMeasureId(comparisonForm.measure);

				return createLocalJob(comparison, comparisonForm.dataset1,
						comparisonForm.dataset2, comparisonService, engine);
			} catch (IOException e) {
				log.error("Internal error writing to disk", e);
				return "Internal error writing to disk";
			}
		}
        else
            return querySlaves(comparisonForm,context); //extra addition due to master slave config 
        
          }
		else {
			log.debug("I am the MASTER");
			String compId;
			compId=querySlaves(comparisonForm, context);
			log.debug("From querySlaves: Comparison ID "+compId);
			return compId;
		}
	}

	/**
	 * 
	 * @param form
	 * @param registry
	 * @param adapterId
	 * @param extractorId
	 * @param measureId
	 * @return
	 */
	public boolean checkRequirements(CompareRegistry registry,
			String adapterId, String extractorId, String measureId) {
		boolean adapter = registry.getAvailableAdaptersIds()
				.contains(adapterId);
		boolean extractor = registry.getAvailableExtractorsIds().contains(
				extractorId);
		boolean measure = registry.getAvailableMeasuresIds()
				.contains(measureId);
		if (adapter && extractor && measure) {
			log.debug("Local requirements fullfilled");
			return true;
		} else {
			log.debug("Local requirements not fullfilled");
			return false;
		}
	}

	/**
	 * 
	 * @param comparisoForm
	 * @param comparisons
	 * @param engine
	 * @return
	 * @throws IOException
	 */
	public String createLocalJob(final PairwiseComparison comparison,
			String dataset1, String dataset2,
			final ComparisonServiceImpl comparisons, ExecutionEngine engine)
			throws IOException {

		// store
		comparisons.addComparison(comparison, dataset1, dataset2);

		engine.submit(comparison, new ComparisonStatusHandler() {

			@Override
			public void onDone(double value) {
				log.debug("Comparison " + comparison.getId()
						+ " done. Result is: " + value);
				comparisons.setStatus(comparison.getId(), ComparisonStatus.DONE);
				comparisons.updateValue(comparison.getId(), value);
			}

			@Override
			public void onStarted() {
				log.debug("Comparison " + comparison.getId() + " started.");
				comparisons.setStatus(comparison.getId(),
						Job.ComparisonStatus.STARTED);
			}

			@Override
			public void onFailed(String msg, Throwable e) {
				log.debug("Comparison " + comparison.getId() + " failed. "
						+ msg + " " + e);
				comparisons.setStatus(comparison.getId(),
						Job.ComparisonStatus.FAILED);

			}

			@Override
			public void onAborted(String msg) {
				log.debug("Comparison " + comparison.getId() + " aborted. "
						+ msg);
				comparisons.setStatus(comparison.getId(),
						Job.ComparisonStatus.ABORTED);
			}
		});

		return comparison.getId();
	}

	private Response queryGetStatusSlaves(String id, String requestUrl,@Context ServletContext context){
		
		
		HttpClient client = new DefaultHttpClient();
		
		String reqUrl=requestUrl+"/"+id+"/status";
		
		log.debug("queryGETStatusSlaves Url: "+reqUrl);
		
		HashIdSlave list= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
		
		HttpGet httpGet=new HttpGet(reqUrl);
		HttpResponse response;
		try {
			
			response = client.execute(httpGet);
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

	         String output,output1=null;
	         log.debug("Response from Slave Server ....GET: \n");
	       
	         while ((output = br.readLine()) != null) {
	        	 output1=output;
	        	 log.debug(output);
	           }
	         
	         //ObjectMapper mapper = new ObjectMapper();
             // Map<String,Object> json=new HashMap<String,Object>();
			 
             /* try {
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
			return Response.status(200).entity(output1).build();
	                        
			
			
			//System.out.println(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
		
	}
	
private Response queryGetValueSlaves(String id, String requestUrl,@Context ServletContext context){
		
		
		HttpClient client = new DefaultHttpClient();
		
		String reqUrl=requestUrl+"/"+id+"/value";
		
		log.debug("queryGETValueSlaves Url: "+reqUrl);
		
		HashIdSlave list= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
		
		HttpGet httpGet=new HttpGet(reqUrl);
		HttpResponse response;
		try {
			
			response = client.execute(httpGet);
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

	         String output,output1=null;
	         log.debug("Response from Slave Server ....GET: \n");
	       
	         while ((output = br.readLine()) != null) {
	        	 output1=output;
	        	 log.debug(output);
	           }
	         
	         //ObjectMapper mapper = new ObjectMapper();
             // Map<String,Object> json=new HashMap<String,Object>();
			 
             /* try {
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
			return Response.status(200).entity(output1).build();
	                        
			
			
			//System.out.println(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
		
	}
	
	
	
private Map<String,Object> queryGetSlaves(String id, String requestUrl,@Context ServletContext context){
		
		
		HttpClient client = new DefaultHttpClient();
		
		String reqUrl=requestUrl+"/"+id+"/value";
		
		log.debug("quesryGETSlaves Url: "+reqUrl);
		
		HashIdSlave list= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
		
		HttpGet httpGet=new HttpGet(reqUrl);
		HttpResponse response;
		try {
			
			response = client.execute(httpGet);
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

	         String output,output1=null;
	         System.out.println("Response from Slave Server ....GET: \n");
	       
	         while ((output = br.readLine()) != null) {
	        	 output1=output;
	        	 System.out.println(output);
	           }
	         
	         ObjectMapper mapper = new ObjectMapper();
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
				}
	                        
			return json;
			
			//System.out.println(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
		
	}
	
	
	/**
	 * 
	 * @param comparison
	 * @return
	 */
	//private String querySlaves(ComparisonForm comparison,
	//		ComparisonForm comparison2,@Context ServletContext context) {
		
		private String querySlaves(ComparisonForm comparison,@Context ServletContext context){
		
		// TODO Auto-generated method stub
		
			//SlavesList slaveList=(SlavesList) context.getAttribute(SlavesList.class.getName());
		    // Collection<Slave> slaves = slaveList.getSlaves();
			
			log.debug("Inside quesrySlaves():");
			HashIdSlave list= (HashIdSlave) context.getAttribute(HashIdSlave.class.getName());
			
		     RankSlaves slaveList=(RankSlaves) context.getAttribute(RankSlaves.class.getName());
		     ArrayList<Slave> slaves=(ArrayList<Slave>) slaveList.getSlaves();
		
		//if (slaves.size() == 0) {
		//	return "No slaves";
		//} else {
			
			//for (Slave slave : slaves) {
			//	log.debug(slave.getUrl());
			//}
			
			HttpClient client = new DefaultHttpClient();
			if(RankSlaves.nextIndex<0){
				RankSlaves.nextIndex=0;
				
			}
			else{
				if(RankSlaves.nextIndex==(slaves.size()-1)){
					RankSlaves.nextIndex=0;
				}
				else
					RankSlaves.nextIndex++;
				
			}
			
			
			////String requestUrl = "http://localhost:8182/api/v1/comparisons";
			String requestUrl=(slaves.get(RankSlaves.nextIndex).getUrl()).toString()+"/comparisons";
			HttpPost httpPost = new HttpPost(requestUrl);
			
			/*HashIdSlave idSlaveobject=new HashIdSlave();
			
			try {
				idSlaveobject.setUrl(new URL(requestUrl));
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			log.debug("MASTER: I am here to send data to slave");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("dataset1",comparison.dataset1));
			nvps.add(new BasicNameValuePair("dataset2",comparison.dataset2));
			nvps.add(new BasicNameValuePair("adapter",comparison.adapter));
			nvps.add(new BasicNameValuePair("extractor",comparison.extractor));
			nvps.add(new BasicNameValuePair("measure",comparison.measure));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpResponse response;
			try {
				
				response = client.execute(httpPost);
				
				BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

		                String output,output1=null;
		               System.out.println("Response from Slave Server ....POST: \n");
		        
		               while ((output = br.readLine()) != null) {
		        	    output1=output;
			            System.out.println(output);
		               }
		        //idSlaveobject.setId(output);
		        //compList.add(idSlaveobject);
		        log.debug("Comparison ID: "+output1+"  Working Slave URL"+requestUrl);
		        list.addTohashList(output1,requestUrl);
		        log.debug("Size of HashList Id-Slave mapping: "+list.gethashList().size());
				return output1;
				
				//System.out.println(response.getEntity().getContent());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		//}
		return null;
	}

	/**
	 * Make temp copy of remote file.
	 * 
	 * @param remoteURL
	 * @return
	 * @throws IOException
	 */
	public File getFile(String remoteURL) throws IOException {
		log.debug("I am inside getFile in Comparison resource");
		
	    URL url = new URL(remoteURL); //commented by smruti
		byte[] buff = new byte[10240];
		int len;
		File file;
		//log.debug("I am inside getFile in Comparison resource");
		//------------------------------------------------------------ The original code-----------------------
		if (url.getPath().isEmpty()
			|| url.getPath().matches(".*/versus[\\d]+.tmp")) {
				file = File.createTempFile("versus", ".tmp");
		} else {
			String filename = new File(url.getPath()).getName().replaceAll(
					"[\\d]+\\.", ".");
			int idx = filename.lastIndexOf(".");
			if (idx > 3) {
				file = File.createTempFile(filename.substring(0, idx),
						filename.substring(idx));
			} else if (idx != -1) {
				file = File.createTempFile("versus", filename.substring(idx));
			} else {
				file = File.createTempFile(filename, ".tmp");
			}
		}
		//-------------------------------------------------------------------------
		
		file.deleteOnExit(); // TODO only gets called when jvm exits
		FileOutputStream fos = new FileOutputStream(file);
		 InputStream is = url.openStream();
		while ((len = is.read(buff)) != -1) {
			fos.write(buff, 0, len);
		}
		is.close();
		fos.close();
		log.debug("");
		return file;
	}

	public static class ComparisonForm {

		@FormParam("dataset1")
		private String dataset1;

		@FormParam("dataset2")
		private String dataset2;

		@FormParam("adapter")
		private String adapter;

		@FormParam("extractor")
		private String extractor;

		@FormParam("measure")
		private String measure;

		@Override
		public String toString() {
			return "dataset1=" + dataset1 + "&dataset2=" + dataset2
					+ "&adapter=" + adapter + "&extractor=" + extractor
					+ "&measure=" + measure;
		}

	}

}
