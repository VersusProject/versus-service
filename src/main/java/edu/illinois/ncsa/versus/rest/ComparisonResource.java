/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import org.jboss.resteasy.annotations.Form;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.ComparisonStatusHandler;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.Job;
import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.Comparison;
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

	@GET
	@Produces("application/json")
	public List<String> list(@Context ServletContext context) {

		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);
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

	/**
	 * Submit new comparison.
	 * 
	 * @param entity
	 * @return
	 */
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String submit(@Form ComparisonForm comparison,
			@Context ServletContext context) {

		CompareRegistry registry = (CompareRegistry) context
				.getAttribute(CompareRegistry.class.getName());
		ExecutionEngine engine = (ExecutionEngine) context
				.getAttribute(ExecutionEngine.class.getName());
		Injector injector = (Injector) context.getAttribute(Injector.class
				.getName());
		ComparisonServiceImpl comparisonService = injector
				.getInstance(ComparisonServiceImpl.class);

		if (checkRequirements(comparison, registry)) {
			try {
				return createLocalJob(comparison, comparisonService, engine);
			} catch (IOException e) {
				log.error("Internal error writing to disk", e);
				return "Internal error writing to disk";
			}
		} else {
			return querySlaves(comparison, comparison);
		}
	}

	/**
	 * 
	 * @param form
	 * @param registry
	 * @return
	 */
	private boolean checkRequirements(ComparisonForm form,
			CompareRegistry registry) {
		boolean adapter = registry.getAvailableAdaptersIds().contains(
				form.adapter);
		boolean extractor = registry.getAvailableExtractors().contains(
				form.extractor);
		boolean measure = registry.getAvailableMeasures()
				.contains(form.measure);
		if (adapter && extractor && measure) {
			log.debug("Local requirements fullfilled:" + form);
			return true;
		} else {
			log.debug("Local requirements not fullfilled" + form);
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
	private String createLocalJob(ComparisonForm comparisoForm,
			final ComparisonServiceImpl comparisons, ExecutionEngine engine)
			throws IOException {
		final PairwiseComparison comparison = new PairwiseComparison();
		comparison.setId(UUID.randomUUID().toString());
		comparison.setFirstDataset(getFile(comparisoForm.dataset1));
		comparison.setSecondDataset(getFile(comparisoForm.dataset2));
		comparison.setAdapterId(comparisoForm.adapter);
		comparison.setExtractorId(comparisoForm.extractor);
		comparison.setMeasureId(comparisoForm.measure);

		// store
		comparisons.addComparison(comparison, comparisoForm.dataset1,
				comparisoForm.dataset2);

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

	/**
	 * 
	 * @param comparison
	 * @param comparison2
	 * @return
	 */
	private String querySlaves(ComparisonForm comparison,
			ComparisonForm comparison2) {
		// TODO Auto-generated method stub
		return null;
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
