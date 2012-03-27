package edu.illinois.ncsa.versus.restlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.ComparisonStatusHandler;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.Job;
import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.DistributionServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Multiple comparisons.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class ComparisonsServerResource extends ServerResource {

	/**
	 * List of comparisons.
	 * 
	 * @return
	 */
	@Get("html")
	public Representation list() {

		// Guice storage
		Injector injector = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		Collection<Comparison> comparisons = comparisonService.listAll();

		if (comparisons.size() == 0) {
			Representation representation = new StringRepresentation(
					"No comparisons", MediaType.TEXT_HTML);
			return representation;
		} else {
			String content = new String("<h3>Versus > Comparisons</h3>"
					+ "<ul>");
			for (Comparison comparison : comparisons) {
				String id = comparison.getId();
				content += "<li><a href='/versus/api/comparisons/" + id + "'>"
						+ id + "</a></li>";
			}
			content += "</ul>";
			Representation representation = new StringRepresentation(content,
					MediaType.TEXT_HTML);
			return representation;
		}
	}
	
	@Get("json")
	public Representation asJSON(){

		String id                               = (String) getRequest().getAttributes().get("id");
		Injector injector                       = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
		Comparison comparison                   = comparisonService.getComparison(id);
		
		try {
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("id", comparison.getId() );
			jsonObject.put("adapter",comparison.getAdapterId() );
			jsonObject.put("extractor",comparison.getExtractorId() );
			jsonObject.put("measure",comparison.getMeasureId() );
			jsonObject.put("status", comparison.getStatus() );
			jsonObject.put("firstDataSet", comparison.getFirstDataset() );
			jsonObject.put("secondDataSet", comparison.getSecondDataset() );
						
			return new JsonRepresentation(jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Error creating json",	MediaType.TEXT_HTML);
		}
	}
	
	
	
	

	/**
	 * Submit new comparison.
	 * 
	 * @param entity
	 * @return
	 */
	@Post
	public Representation submit(Representation entity) {
		getLogger().log(Level.INFO, "POST submission : \n" + entity.toString());
		// create comparison
		Comparison comparison;
		try {
			comparison = createComparison(entity);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Error loading input files\n", e);
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Error retrieving file\n",
					MediaType.TEXT_PLAIN);
		}

		// check local registry
		if (checkRequirements(comparison)) {
			try {
				return createLocalJob(comparison);
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Error creating local job", e);
			}
		} else {
			return querySlaves(entity, comparison);
		}
		return null;
	}

	private Comparison createComparison(Representation entity)
			throws IOException {
		Comparison comparison = new Comparison();
		Form form = new Form(entity);
		comparison.setId(UUID.randomUUID().toString());

		comparison.setFirstDataset(form.getFirstValue("dataset1"));
		comparison.setSecondDataset(form.getFirstValue("dataset2"));

		comparison.setAdapterId(form.getFirstValue("adapter"));
		comparison.setExtractorId(form.getFirstValue("extractor"));
		comparison.setMeasureId(form.getFirstValue("measure"));
		return comparison;
	}

	private Representation createLocalJob(Comparison comparison) throws IOException {

		// TODO find a better way to manage comparisons
		PairwiseComparison pairwiseComparison = new PairwiseComparison();
		pairwiseComparison.setId(comparison.getId());
		pairwiseComparison
				.setFirstDataset(getFile(comparison.getFirstDataset()));
		pairwiseComparison.setSecondDataset(getFile(comparison
				.getSecondDataset()));
		pairwiseComparison.setAdapterId(comparison.getAdapterId());
		pairwiseComparison.setExtractorId(comparison.getExtractorId());
		pairwiseComparison.setMeasureId(comparison.getMeasureId());

		// Guice storage
		Injector injector = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		comparisonService.addComparison(comparison);

		// Submit to engine
		submit(pairwiseComparison);
		setStatus(Status.SUCCESS_CREATED);
		
		String comparisonURL = getRequest().getResourceRef().getIdentifier() + "/" + comparison.getId();
		
		Representation representation = new StringRepresentation(comparisonURL,	MediaType.TEXT_PLAIN);
		representation.setLocationRef(comparisonURL);
		return representation;
	}
	
	public void createJob(Comparison comparison) throws IOException {

		// TODO find a better way to manage comparisons
		PairwiseComparison pairwiseComparison = new PairwiseComparison();
		pairwiseComparison.setId(comparison.getId());
		pairwiseComparison
				.setFirstDataset(getFile(comparison.getFirstDataset()));
		pairwiseComparison.setSecondDataset(getFile(comparison
				.getSecondDataset()));
		pairwiseComparison.setAdapterId(comparison.getAdapterId());
		pairwiseComparison.setExtractorId(comparison.getExtractorId());
		pairwiseComparison.setMeasureId(comparison.getMeasureId());

		// Guice storage
		Injector injector = Guice.createInjector(new RepositoryModule());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
		comparisonService.addComparison(comparison);

		// Submit to engine
		submit(pairwiseComparison);
		setStatus(Status.SUCCESS_CREATED);
		
	}
	
	
	/**
	 * Find which slaves support requested methods. Forward to the first slave
	 * in the list.
	 * 
	 * @param entity
	 * @param comparison
	 * @return
	 */
	private Representation querySlaves(Representation entity,
			Comparison comparison) {
		List<Slave> slaves = ((ServerApplication) getApplication()).getSlaves();
		List<Slave> supportingSlaves = new ArrayList<Slave>();
		for (Slave slave : slaves) {
			if (supportComparison(slave, comparison)) {
				getLogger()
						.log(Level.INFO,
								"Adding "
										+ slave.getUrl()
										+ " to slaves that can support support the requested comparison.");
				supportingSlaves.add(slave);
			}
		}
		// rank slaves
		supportingSlaves = RankSlaves.rank(supportingSlaves);
		// forward to first slave
		if (supportingSlaves.size() == 0) {
			return new StringRepresentation(
					"No slaves are available that can handle this comparison",
					MediaType.TEXT_PLAIN);
		} else {
			Slave slave = supportingSlaves.get(0);
			String slaveUrl = slave.getUrl().toString() + "/comparisons";
			getLogger().log(Level.INFO,
					"Forwarding comparison request to " + slaveUrl);
			try {
				ClientResource clientResource = new ClientResource(slaveUrl);
				return clientResource
						.post(getComparisonRepresentation(comparison));
			} catch (ResourceException e) {
				getLogger().log(Level.SEVERE,
						"Error connecting to server " + slaveUrl, e);
				return new StringRepresentation("Error connecting to server "
						+ slaveUrl, MediaType.TEXT_PLAIN);
			}
		}
	}

	private boolean supportComparison(Slave slave, Comparison comparison) {
		String adapterUrl = slave.getUrl().toString() + "/adapters/"
				+ comparison.getAdapterId();
		String extractorUrl = slave.getUrl().toString() + "/extractors/"
				+ comparison.getExtractorId();
		String measureUrl = slave.getUrl().toString() + "/measures/"
				+ comparison.getMeasureId();
		if (supportModule(adapterUrl) && supportModule(extractorUrl)
				&& supportModule(measureUrl)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean supportModule(String url) {
		getLogger().log(Level.INFO, "Checking for " + url);
		try {
			ClientResource clientResource = new ClientResource(url);
			clientResource.get();
			if (clientResource.getStatus().isSuccess()) {
				getLogger().log(Level.INFO, "Module " + url + " founds");
				return true;
			} else {
				getLogger().log(Level.INFO, "Module " + url + " not found");
				return false;
			}
		} catch (Exception e) {
			getLogger().log(Level.INFO,
					"Error querying client to see if it supports " + url);
			return false;
		}
	}

	public boolean checkRequirements(Comparison comparison) {
		CompareRegistry registry = ((ServerApplication) getApplication())
				.getRegistry();
		if (registry.getAvailableAdaptersIds().contains(
				comparison.getAdapterId())) {
			getLogger().log(Level.INFO,
					"Local " + comparison.getAdapterId() + " found");
			return true;
		} else {
			getLogger().log(Level.INFO,
					"Local " + comparison.getAdapterId() + " not found");
			return false;
		}
	}

	/**
	 * Make temp copy of remote file.
	 * 
	 * @param remoteURL
	 * @return
	 * @throws IOException
	 */
	private File getFile(String remoteURL) throws IOException {
		URL url = new URL(remoteURL);
		byte[] buff = new byte[10240];
		int len;
		File file;
		if (url.getPath().isEmpty() || url.getPath().matches(".*/versus[\\d]+.tmp")) {
			file = File.createTempFile("versus", ".tmp");
		} else {
			String filename = new File(url.getPath()).getName().replaceAll("[\\d]+\\.", ".");
			int idx = filename.lastIndexOf(".");
			if (idx > 3) {				
				file = File.createTempFile(filename.substring(0, idx), filename.substring(idx));
			} else if (idx != -1) {
				file = File.createTempFile("versus", filename.substring(idx));
			} else {
				file = File.createTempFile(filename, ".tmp");
			}
		}
		file.deleteOnExit(); // TODO only gets called when jvm exits
		FileOutputStream fos = new FileOutputStream(file);
		InputStream is = url.openStream();
		while ((len = is.read(buff)) != -1) {
			fos.write(buff, 0, len);
		}
		is.close();
		fos.close();
		return file;
	}

	/**
	 * Submit to execution engine.
	 * 
	 * @param comparison
	 */
	private void submit(final PairwiseComparison comparison) {

		ExecutionEngine engine = ((ServerApplication) getApplication())
				.getEngine();
		final ComparisonServiceImpl comparisonService = ServerApplication
				.getInjector().getInstance(ComparisonServiceImpl.class);

		engine.submit(comparison, new ComparisonStatusHandler() {

			@Override
			public void onDone(double value) {
				getLogger().log(
						Level.INFO,
						"Comparison " + comparison.getId()
								+ " done. Result is: " + value);
				comparisonService.setStatus(comparison.getId(),
						ComparisonStatus.DONE);
				comparisonService.updateValue(comparison.getId(), value);
			}

			@Override
			public void onStarted() {
				getLogger().log(Level.INFO,
						"Comparison " + comparison.getId() + " started.");
				comparisonService.setStatus(comparison.getId(),
						Job.ComparisonStatus.STARTED);
			}

			@Override
			public void onFailed(String msg, Throwable e) {
				getLogger().log(
						Level.INFO,
						"Comparison " + comparison.getId() + " failed. " + msg
								+ " " + e);
				comparisonService.setStatus(comparison.getId(),
						Job.ComparisonStatus.FAILED);

			}

			@Override
			public void onAborted(String msg) {
				getLogger()
						.log(Level.INFO,
								"Comparison " + comparison.getId()
										+ " aborted. " + msg);
				comparisonService.setStatus(comparison.getId(),
						Job.ComparisonStatus.ABORTED);
			}
		});
	}

	private static Representation getComparisonRepresentation(
			Comparison comparison) {
		Form form = new Form();
		form.add("id", comparison.getId());
		form.add("dataset1", comparison.getFirstDataset());
		form.add("dataset2", comparison.getSecondDataset());
		form.add("adapter", comparison.getAdapterId());
		form.add("extractor", comparison.getExtractorId());
		form.add("measure", comparison.getMeasureId());
		return form.getWebRepresentation();
	}
}
