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
//import java.net.InetAddress;
//import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URLDecoder;
//import java.net.UnknownHostException;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
 * @author Luigi Marini , Smruti Padhy
 * Implementation with Round Robin
 * 
 */
@Path("/comparisons")
public class ComparisonResource1 {

	private static Log log = LogFactory.getLog(ComparisonResource1.class);

	@GET
	@Produces("application/json")
	public List<String> list(@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		log.debug("I am inside GET list ");

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

		// Map<String, Object> json = new HashMap<String, Object>();
		Response json;

		HashIdSlave compList = (HashIdSlave) context
				.getAttribute(HashIdSlave.class.getName());

		log.debug("Hash List size: " + compList.gethashList().size()
				+ "  containsKey : " + compList.gethashList().containsKey(id));

		if (compList.gethashList().containsKey(id)) {

			log.debug("Server that performed the Comparison: URL: "
					+ compList.gethashList().get(id));

			json = queryGetStatusSlaves(id, compList.gethashList().get(id),
					context); // querying status from the Server

			log.debug("Received Status from Server: " + json);

			return json;
		} else {

			/*
			 * log.debug("Comparison status: "+c.getStatus());
			 * 
			 * if( c.getStatus() != ComparisonStatus.DONE ){ //
			 * while(c.getStatus()!=ComparisonStatus.DONE){ try {
			 * //Thread.sleep(5); synchronized(c){ c.wait(); } } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 * 
			 * return json; }
			 */

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

		log.debug("I am inside getValue: /id/value ");

		Response json;

		HashIdSlave compList = (HashIdSlave) context
				.getAttribute(HashIdSlave.class.getName());

		log.debug("hash List size: " + compList.gethashList().size()
				+ "  containsKey" + compList.gethashList().containsKey(id));

		if (compList.gethashList().containsKey(id)) {

			log.debug("Server which did this Comparison:Url: "
					+ compList.gethashList().get(id));

			json = queryGetValueSlaves(id, compList.gethashList().get(id),
					context);

			log.debug("Received Value from Server: " + json);

			return json;
		} else {
			Comparison c = comparisonService.getComparison(id);

			if (c == null) {
				log.debug("Comparison not found " + id);
				return Response.status(404)
						.entity("Comparison " + id + " not found.").build();
			} else {
				log.debug("Comparison found " + id);

				log.debug("Comparison status: " + c.getStatus());

				if (c.getStatus() != ComparisonStatus.DONE) {
					// while(c.getStatus()!=ComparisonStatus.DONE){
					try {
						// Thread.sleep(5);
						synchronized (c) {
							c.wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return Response.status(200).entity(c.getValue()).build();
			}
		}
	}

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

		SlavesList slaveList = (SlavesList) context
				.getAttribute(SlavesList.class.getName());
		List<Slave> slaves = slaveList.getSlaves();

		FileMap fmap = (FileMap) context.getAttribute(FileMap.class.getName());

		// log.debug("SLAVE URL= "+slaves.get(0).getUrl());

		Iterator<Slave> itr = slaves.iterator();
		HttpClient client1 = new DefaultHttpClient();
		// CompareHashObject co=new CompareHashObject();

		/*
		 * int i = 0; while (itr.hasNext()) { Slave s = itr.next();
		 * s.setStatus(checkifbusy(s, context, client1)); if (s.getStatus() ==
		 * true) i++; } boolean AllSlavebusy = false;
		 * 
		 * if (i == slaves.size()) { AllSlavebusy = true; }
		 */
		// if (slaves.size() == 0 || AllSlavebusy) {

		File dataset1 = null, dataset2 = null;
		String fname1 = "", fname2 = "";
		FileObject fo = null;
		fname1 = getFilenameFromURL(comparisonForm.dataset1);
		fname2 = getFilenameFromURL(comparisonForm.dataset2);

		// log.debug("dataset1 URL= "+ comparisonForm.dataset1+
		// "dataset2 URL ="+ comparisonForm.dataset2);

		String name, path;
		String cid = "";
		String dataset1Url = "", dataset2Url = "";
		String uname = "", upath = "";
		if (slaves.size() == 0) {
			if (checkRequirements(registry, comparisonForm.adapter,
					comparisonForm.extractor, comparisonForm.measure)) {
				try {
					log.debug("I am ready to perform the comparison");
					final PairwiseComparison comparison = new PairwiseComparison();
					comparison.setId(UUID.randomUUID().toString());
					comparison.setAdapterId(comparisonForm.adapter);
					comparison.setExtractorId(comparisonForm.extractor);
					comparison.setMeasureId(comparisonForm.measure);

					if (!fmap.getMap().isEmpty()) { // when filemap is not empty
						log.debug("Fmap is not empty");

						if (fmap.getMap().containsKey(fname1)) {
							path = fmap.getMap().get(fname1).getPath();

							name = fmap.getMap().get(fname1).getVid();
							comparison.setFirstDataset(new File(path));
							dataset1Url = "file:///" + path;
							log.debug("File is found: dataset1Url ="
									+ dataset1Url);
						} else {
							dataset1 = getFile(comparisonForm.dataset1);
							if (dataset1.isFile()) {
								comparison.setFirstDataset(dataset1);
								fo = new FileObject();
								fo.setName(getFilenameFromURL(comparisonForm.dataset1));
								fo.setVid(dataset1.getName());
								upath = dataset1.getPath();
								// upath= upath.replaceAll("\\\\", "/");
								// upath.replaceAll(File.separator, "/");
								// fo.setPath(dataset1.getPath());
								fo.setPath(upath);
								fmap.addMap(
										getFilenameFromURL(comparisonForm.dataset1),
										fo);
								dataset1Url = comparisonForm.dataset1;
								log.debug("File is not found:Get remote file: dataset1Url ="
										+ dataset1Url);
							}
						}

						if (fmap.getMap().containsKey(fname2)) {
							path = fmap.getMap().get(fname2).getPath();
							name = fmap.getMap().get(fname2).getVid();
							comparison.setSecondDataset(new File(path));
							dataset2Url = "file:///" + path;
							log.debug("File is found: dataset2Url ="
									+ dataset2Url);

						} else {
							dataset2 = getFile(comparisonForm.dataset2);
							if (dataset2.isFile()) {
								if (dataset2.isFile()) {
									comparison.setSecondDataset(dataset2);
									fo = new FileObject();
									fo.setName(getFilenameFromURL(comparisonForm.dataset2));
									fo.setVid(dataset2.getName());
									upath = dataset2.getPath();
									// upath= upath.replaceAll("\\\\", "/");
									fo.setPath(upath);

									// fo.setPath(dataset2.getPath());
									fmap.addMap(
											getFilenameFromURL(comparisonForm.dataset2),
											fo);
									dataset2Url = comparisonForm.dataset2;
									log.debug("File is not found:Get remote file: dataset2Url ="
											+ dataset2Url);
								}
							}
						}
					} //
					else // when map is empty
					{
						log.debug("fmap is empty");

						dataset1 = getFile(comparisonForm.dataset1);
						dataset2 = getFile(comparisonForm.dataset2);

						if (dataset1.isFile()) {
							comparison.setFirstDataset(dataset1);
							fo = new FileObject();
							fo.setName(getFilenameFromURL(comparisonForm.dataset1));
							fo.setVid(dataset1.getName());
							upath = dataset1.getPath();
							// upath= upath.replaceAll("\\\\", "/");
							// upath=upath.replace("\\", File.separator);

							fo.setPath(upath);
							// fo.setPath(dataset1.getPath());
							log.debug("dataset1.getName()="
									+ dataset1.getName()
									+ " dataset1.getPath()="
									+ dataset1.getPath() + " upath=" + upath);
							fmap.addMap(
									getFilenameFromURL(comparisonForm.dataset1),
									fo);
							dataset1Url = comparisonForm.dataset1;
						}
						if (dataset2.isFile()) {
							if (dataset2.isFile()) {
								comparison.setSecondDataset(dataset2);
								fo = new FileObject();
								fo.setName(getFilenameFromURL(comparisonForm.dataset2));
								fo.setVid(dataset2.getName());
								upath = dataset2.getPath();
								// upath= upath.replaceAll("\\\\", "/");

								fo.setPath(upath);

								// fo.setPath(dataset2.getPath());
								fmap.addMap(
										getFilenameFromURL(comparisonForm.dataset2),
										fo);
								dataset2Url = comparisonForm.dataset2;
								log.debug("dataset2.getName()="
										+ dataset2.getName()
										+ " dataset2.getPath()="
										+ dataset2.getPath() + " upath ="
										+ upath);
							}
						}
					}

					// return createLocalJob(comparison,
					// comparisonForm.dataset1,
					// comparisonForm.dataset2, comparisonService, engine);
					cid = createLocalJob(comparison, dataset1Url, dataset2Url,
							comparisonService, engine);
					// co.setCid(cid);
					// co.setStatus(comparisonService.getComparison(cid).getStatus());
					// co.setValue(Double.parseDouble(comparisonService.getComparison(cid).getValue()));

					return cid;
					// return a JSON Object
				} catch (IOException e) {
					log.error("Internal error writing to disk", e);
					return "Internal error writing to disk";
				}
			} else
				// return querySlaves(comparisonForm,context); //extra addition
				// due to master slave config
				return "Modules not Supported by any Slaves";
		} else {
			log.debug("I am the MASTER");
			String compId;
			compId = querySlaves(comparisonForm, context);
			if (compId.equals("Modules not Supported by any Slaves")) {
				return "Modules not Supported by any Slaves";
			} else {
				log.debug("From querySlaves: Comparison ID " + compId);
				return compId;
			}
		}
	}

	String getFilenameFromURL(String url) {
		String fname;
		String c[] = url.split("/");
		for (int i = 0; i < c.length; i++) {
			// log.debug("c["+i+"]="+c[i]);
		}

		fname = c[c.length - 1];
		// log.debug("c.length="+c.length+"fname ="+fname);
		return fname;
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
			double startTime = 0.0, endTime = 0.0, timeElapsed;

			@Override
			public void onDone(double value) {
				log.debug("Comparison " + comparison.getId()
						+ " done. Result is: " + value);
				comparisons.setStatus(comparison.getId(), ComparisonStatus.DONE);
				comparisons.updateValue(comparison.getId(), value);
				endTime = System.currentTimeMillis();
				timeElapsed = endTime - startTime;
				log.debug("Time Taken to perfom the comparison "
						+ comparison.getId() + "= " + timeElapsed);
			}

			@Override
			public void onStarted() {
				startTime = System.currentTimeMillis();
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

	private Response queryGetStatusSlaves(String id, String requestUrl,
			@Context ServletContext context) {

		HttpClient client = new DefaultHttpClient();

		String reqUrl = requestUrl + "/" + id + "/status";

		log.debug("queryGETStatusSlaves URL: " + reqUrl);

		// HashIdSlave list= (HashIdSlave)
		// context.getAttribute(HashIdSlave.class.getName());

		HttpGet httpGet = new HttpGet(reqUrl);
		HttpResponse response;
		try {

			response = client.execute(httpGet);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output, output1 = null;
			log.debug("Response:STATUS from Slave Server : \n");

			while ((output = br.readLine()) != null) {
				output1 = output;
				log.debug(output);
			}

			return Response.status(200).entity(output1).build();

			// System.out.println(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private Response queryGetValueSlaves(String id, String requestUrl,
			@Context ServletContext context) {

		HttpClient client = new DefaultHttpClient();

		String reqUrl = requestUrl + "/" + id + "/value";

		log.debug("queryGETValueSlaves URL: " + reqUrl);

		// HashIdSlave list= (HashIdSlave)
		// context.getAttribute(HashIdSlave.class.getName());

		HttpGet httpGet = new HttpGet(reqUrl);
		HttpResponse response;
		try {

			response = client.execute(httpGet);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output, output1 = null;
			log.debug("Response: VALUE from Slave Server: \n");

			while ((output = br.readLine()) != null) {
				output1 = output;
				log.debug(output);
			}

			return Response.status(200).entity(output1).build();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return null;

	}

	@SuppressWarnings("unused")
	private Map<String, Object> queryGetSlaves(String id, String requestUrl,
			@Context ServletContext context) {

		HttpClient client = new DefaultHttpClient();

		String reqUrl = requestUrl + "/" + id + "/value";

		log.debug("queryGETSlaves Url: " + reqUrl);

		// HashIdSlave list= (HashIdSlave)
		// context.getAttribute(HashIdSlave.class.getName());

		HttpGet httpGet = new HttpGet(reqUrl);

		HttpResponse response;
		try {

			response = client.execute(httpGet);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output, output1 = null;
			System.out.println("Response from Slave Server ....GET: \n");

			while ((output = br.readLine()) != null) {
				output1 = output;
				log.debug(output);
			}

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> json = new HashMap<String, Object>();

			try {
				json = mapper.readValue(output1,
						new TypeReference<Map<String, Object>>() {
						});
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

			// System.out.println(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 
	 * @param comparison
	 * @return
	 */
	private String querySlaves(ComparisonForm comparison,
			@Context ServletContext context) {

		log.debug("Inside querySlaves():");
		HashIdSlave list = (HashIdSlave) context.getAttribute(HashIdSlave.class
				.getName());
		
		//RankSlaves slaveList = (RankSlaves) context
		//		.getAttribute(RankSlaves.class.getName());
		SlavesList slaveList = (SlavesList) context
						.getAttribute(SlavesList.class.getName());
		RankSlaves rank =(RankSlaves)context.getAttribute(RankSlaves.class.getName());


		ArrayList<Slave> slaves = (ArrayList<Slave>) slaveList.getSlaves();

		HttpClient client = new DefaultHttpClient();


		int ssize = slaveList.getSlaves().size();
		boolean flag = false;
		

		do {
			/*if (RankSlaves.nextIndex < 0) {
				RankSlaves.nextIndex = 0;
			} else {
				if (RankSlaves.nextIndex == (slaves.size() - 1)) {
					RankSlaves.nextIndex = 0;
				} else
					RankSlaves.nextIndex++;
			}*/
			rank.setNextIndex(slaves.size()-1);
			//log.debug("RankSlaves.nextIndex= " + RankSlaves.nextIndex);
			log.debug("RankSlaves.nextIndex= " + rank.getNextIndex());
			// if (checkifbusy(slaves.get(RankSlaves.nextIndex), context,
         	// client) == false) {

			//if (supportComparison(slaves.get(RankSlaves.nextIndex), comparison,
			//		context, client)) {
			if (supportComparison(slaves.get(rank.getNextIndex()), comparison,
						context, client)) {
				log.debug("querySlaves(): ADAPTER, EXTRACTOR,MEASURE Supported");
				flag = true;
				break;
			}
			// }
			ssize--;
			// }
		} while (ssize != 0);

		// //String requestUrl = "http://localhost:8182/api/v1/comparisons";
		if (flag == false) {
			return "Modules not Supported by any Slaves";

		} else {
			//String requestUrl = (slaves.get(RankSlaves.nextIndex).getUrl())
			//		.toString() + "/comparisons";
			String requestUrl = (slaves.get(rank.getNextIndex()).getUrl())
					.toString() + "/comparisons";
			HttpPost httpPost = new HttpPost(requestUrl);

			log.debug("MASTER: I am here to send data to slave");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("dataset1", comparison.dataset1));
			nvps.add(new BasicNameValuePair("dataset2", comparison.dataset2));
			nvps.add(new BasicNameValuePair("adapter", comparison.adapter));
			nvps.add(new BasicNameValuePair("extractor", comparison.extractor));
			nvps.add(new BasicNameValuePair("measure", comparison.measure));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpResponse response;
			try {

				response = client.execute(httpPost);

				// read the json object from stream
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));

				String output, output1 = null;
				log.debug("Response from Slave Server : " + requestUrl
						+ " ..POST: ");

				while ((output = br.readLine()) != null) {
					output1 = output;
					log.debug(output);
				}
				// idSlaveobject.setId(output);
				// compList.add(idSlaveobject);

				log.debug("Comparison ID: " + output1 + "  Working Slave URL"
						+ requestUrl);

				list.addTohashList(output1, requestUrl);
				// CompareHashObject co=new CompareHashObject();
				// co.setCid(output1);
				// compList.addToList(output1, co);

				// log.debug("Size of HashList Id-Slave mapping: "
				// + compList.gethashList().size());
				log.debug("Size of HashList Id-Slave mapping: "
						+ list.gethashList().size());

				return output1;

				// System.out.println(response.getEntity().getContent());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// }//if all modules supported
		}
		return null;
	}

	private boolean checkifbusy(Slave slave, @Context ServletContext context,
			HttpClient client) {
		String statusUrl = slave.getUrl().toString() + "/status";
		log.debug("Checking busy status of " + statusUrl);
		HttpGet httpGet = new HttpGet(statusUrl);
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
			log.debug(response.getEntity().getContent().toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output, output1 = null;
			// log.debug("Response from Slave Server : "+requestUrl+" ..POST: ");

			while ((output = br.readLine()) != null) {
				output1 = output;
				log.debug(output);
			}
			if (output1.equals("false")) {

				// if(response.getEntity().getContent().equals("false")){
				log.debug("server is not busy");
				return false;
			} else {
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	private boolean supportComparison(Slave slave, ComparisonForm comparison,
			@Context ServletContext context, HttpClient client) {
		String adapterUrl = slave.getUrl().toString() + "/adapters/"
				+ comparison.adapter;
		String extractorUrl = slave.getUrl().toString() + "/extractors/"
				+ comparison.extractor;
		String measureUrl = slave.getUrl().toString() + "/measures/"
				+ comparison.measure;
		log.debug("adapterUrl: " + adapterUrl);
		log.debug("extractorUrl: " + extractorUrl);
		log.debug("measureUrl: " + measureUrl);
		if (supportModule(adapterUrl, client)
				&& supportModule(extractorUrl, client)
				&& supportModule(measureUrl, client)) {
			log.debug("All Modules Supported");
			return true;
		} else
			return false;

	}

	private boolean supportModule(String url, HttpClient client) {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
		} catch (IllegalStateException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		String output, output1 = null;
		log.debug("Response from Slave Server ....GET module response:");

		try {
			while ((output = br.readLine()) != null) {
				output1 = output;
				log.debug(output);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> json = new HashMap<String, Object>();

		try {
			json = mapper.readValue(output1,
					new TypeReference<Map<String, Object>>() {
					});
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

		if (json == null || json.containsKey("Error")) {
			return false;
		} else
			return true;

	}

	/**
	 * Make temp copy of remote file.
	 * 
	 * @param remoteURL
	 * @return
	 * @throws IOException
	 */
	public static File getFile(String remoteURL) throws IOException {
		log.debug("I am inside getFile in Comparison resource");

		URL url = new URL(remoteURL);
		byte[] buff = new byte[10240];
		int len;
		File file;
		// log.debug("I am inside getFile in Comparison resource");
		// ------------------------------------------------------------ The
		// original code-----------------------
		log.debug("remoteURL=" + remoteURL + " url.getPath= " + url.getPath());
		if (url.getPath().isEmpty()
				|| url.getPath().matches(".*/versus[\\d]+.tmp")) {
			file = File.createTempFile("versus", ".tmp");
		} else {
			String filename = new File(url.getPath()).getName().replaceAll(
					"[\\d]+\\.", ".");
			log.debug("filename:" + filename);
			int idx = filename.lastIndexOf(".");
			if (idx > 3) {
				file = File.createTempFile(filename.substring(0, idx),
						filename.substring(idx));
				log.debug("filePath:=" + file.getAbsolutePath() + "file:="
						+ file.getName());
			} else if (idx != -1) {
				file = File.createTempFile("versus", filename.substring(idx));
			} else {
				file = File.createTempFile(filename, ".tmp");
				log.debug("filePath1:=" + file.getAbsolutePath() + " file1:="
						+ file.getName());
			}
		}
		// -------------------------------------------------------------------------

		// file.deleteOnExit(); // TODO only gets called when jvm exits
		FileOutputStream fos = new FileOutputStream(file);
		InputStream is = url.openStream();
		while ((len = is.read(buff)) != -1) {
			fos.write(buff, 0, len);
		}
		is.close();
		fos.close();
		log.debug("file Pathname" + file.getAbsolutePath());
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
