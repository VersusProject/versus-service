package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Router;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Main restlet application.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class ServerApplication extends Application {

	private final CompareRegistry registry = new CompareRegistry();
	private Properties properties;
	private final List<Slave> slaves = new ArrayList<Slave>();
	private String masterURL;
	private final ExecutionEngine engine = new ExecutionEngine();
	private int port;
	private static Injector injector;

	public ServerApplication() {
		super();
		port = 8080; // FIXME needs to be dynamic
		injector = Guice.createInjector(new RepositoryModule());
	}

	public ServerApplication(int port) {
		this();
		this.port = port;
		try {
			this.properties = PropertiesUtil.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		masterURL = properties.getProperty("master");

		if (SimpleServer.getMaster() != null) {
			masterURL = SimpleServer.getMaster();
			try {
				registerWithMaster();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void registerWithMaster() throws UnknownHostException {
		// TODO find a better way of getting the full url
		String slaveURL = "http://" + InetAddress.getLocalHost().getHostName()
				+ ":" + port + "/versus";
		getLogger().info("Connecting to " + slaveURL);
		ClientResource masterResource = new ClientResource(masterURL
				+ "/slaves");
		Form form = new Form();
		form.add("url", slaveURL);
		masterResource.post(form.getWebRepresentation());
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/comparisons", ComparisonsServerResource.class);
		router.attach("/comparisons/{id}/status",
				ComparisonStatusServerResource.class);
		router.attach("/comparisons/{id}", ComparisonServerResource.class);
		router.attach("/adapters", AdaptersServerResource.class);
		router.attach("/adapters/{id}", AdapterServerResource.class);
		router.attach("/extractors", ExtractorsServerResource.class);
		router.attach("/extractors/{id}", ExtractorServerResource.class);
		router.attach("/measures", MeasuresServerResource.class);
		router.attach("/measures/{id}", MeasureServerResource.class);
		router.attach("/slaves", SlavesServerResource.class);
		router.attach("/files/upload", UploadServerResource.class);
		router.attach("/files/{id}", FileServerResource.class);
		router.attachDefault(VersusServerResource.class);
		return router;
	}

	public Collection<Measure> getMeasures() {
		return registry.getAvailableMeasures();
	}

	public Collection<Adapter> getAdapters() {
		return registry.getAvailableAdapters();
	}

	public Collection<Extractor> getExtractors() {
		return registry.getAvailableExtractors();
	}

	public Properties getProperties() {
		return properties;
	}

	public List<Slave> getSlaves() {
		return slaves;
	}

	public ExecutionEngine getEngine() {
		return engine;
	}

	public CompareRegistry getRegistry() {
		return registry;
	}

	public void addSlave(String url) {
		if (!slaves.contains(url)) {
			slaves.add(new Slave(url));
		}
	}

	public static Injector getInjector() {
		return injector;
	}
}
