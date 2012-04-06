package edu.illinois.ncsa.versus.restlet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

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
import edu.illinois.ncsa.versus.restlet.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.restlet.adapter.AdapterServerResource;
import edu.illinois.ncsa.versus.restlet.adapter.AdaptersServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonStatusServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonSupportServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonValueServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonsServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorsServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasureDescriptor;
import edu.illinois.ncsa.versus.restlet.measure.MeasureServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasuresServerResource;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Main restlet application.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ServerApplication extends Application {

    private final CompareRegistry registry = new CompareRegistry();

    private final HashMap<String, Slave> slaves = new HashMap<String, Slave>();

    private String masterURL;

    private final ExecutionEngine engine = new ExecutionEngine();

    private int port;

    private String baseUrl;

    private static final Injector injector =
            Guice.createInjector(new RepositoryModule());

    public ServerApplication(int port, String baseUrl) {
        this(port, baseUrl, null);
    }

    public ServerApplication(int port, String baseUrl, String masterUrl) {
        super();
        this.port = port;
        this.baseUrl = baseUrl;
        this.masterURL = masterUrl;

        if (masterURL != null) {
            try {
                registerWithMaster();
            } catch (UnknownHostException e) {
                getLogger().log(Level.SEVERE, "Cannot register with master", e);
            }
        }
    }

    private void registerWithMaster() throws UnknownHostException {
        String slaveURL = "http://"
                + InetAddress.getLocalHost().getHostAddress() + ":" + port
                + baseUrl;
        getLogger().log(Level.INFO, "Registering slave {0} to {1}",
                new Object[]{slaveURL, masterURL});
        ClientResource masterResource = new ClientResource(masterURL
                + SlavesServerResource.URL);
        Form form = new Form();
        form.add("url", slaveURL);
        masterResource.post(form.getWebRepresentation());
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach(AdaptersServerResource.PATH_TEMPLATE, AdaptersServerResource.class);
        router.attach(AdapterServerResource.PATH_TEMPLATE, AdapterServerResource.class);
        router.attach(ExtractorsServerResource.PATH_TEMPLATE, ExtractorsServerResource.class);
        router.attach(ExtractorServerResource.PATH_TEMPLATE, ExtractorServerResource.class);
        router.attach(MeasuresServerResource.PATH_TEMPLATE, MeasuresServerResource.class);
        router.attach(MeasureServerResource.PATH_TEMPLATE, MeasureServerResource.class);

        router.attach(SlavesServerResource.PATH_TEMPLATE, SlavesServerResource.class);

        router.attach(ComparisonsServerResource.PATH_TEMPLATE, ComparisonsServerResource.class);
        router.attach("/comparisons/{id}/status",
                ComparisonStatusServerResource.class);
        router.attach("/comparisons/{id}/value",
                ComparisonValueServerResource.class);
        router.attach(ComparisonServerResource.PATH_TEMPLATE, ComparisonServerResource.class);
        router.attach(ComparisonSupportServerResource.PATH_TEMPLATE, ComparisonSupportServerResource.class);

        router.attach("/files/upload", UploadServerResource.class);
        router.attach("/files/{id}", FileServerResource.class);
        router.attachDefault(VersusServerResource.class);
        return router;
    }

    public AdapterDescriptor getAdapter(String id) throws NotFoundException {
        Adapter adapter = registry.getAdapter(id);
        if (adapter != null) {
            return new AdapterDescriptor(adapter);
        }

        for (Slave slave : slaves.values()) {
            AdapterDescriptor ad = slave.getAdapter(id);
            if (ad != null) {
                return ad;
            }
        }
        throw new NotFoundException();
    }

    public HashSet<AdapterDescriptor> getAdapters() {
        Collection<Adapter> adapters = registry.getAvailableAdapters();
        HashSet<AdapterDescriptor> result =
                new HashSet<AdapterDescriptor>(adapters.size());

        for (Adapter adapter : adapters) {
            result.add(new AdapterDescriptor(adapter));
        }

        for (Slave slave : slaves.values()) {
            result.addAll(slave.getAdapters());
        }
        return result;
    }

    public ExtractorDescriptor getExtractor(String id) throws NotFoundException {
        Extractor extractor = registry.getExtractor(id);
        if (extractor != null) {
            return new ExtractorDescriptor(extractor, registry);
        }

        for (Slave slave : slaves.values()) {
            ExtractorDescriptor ed = slave.getExtractor(id);
            if (ed != null) {
                return ed;
            }
        }
        throw new NotFoundException();
    }

    public HashSet<ExtractorDescriptor> getExtractors() {
        Collection<Extractor> extractors = registry.getAvailableExtractors();
        HashSet<ExtractorDescriptor> result =
                new HashSet<ExtractorDescriptor>(extractors.size());

        for (Extractor extractor : extractors) {
            result.add(new ExtractorDescriptor(extractor, registry));
        }

        for (Slave slave : slaves.values()) {
            result.addAll(slave.getExtractors());
        }
        return result;
    }

    public MeasureDescriptor getMeasure(String id) throws NotFoundException {
        Measure measure = registry.getMeasure(id);
        if (measure != null) {
            return new MeasureDescriptor(measure);
        }

        for (Slave slave : slaves.values()) {
            MeasureDescriptor md = slave.getMeasure(id);
            if (md != null) {
                return md;
            }
        }
        throw new NotFoundException();
    }

    public HashSet<MeasureDescriptor> getMeasures() {
        Collection<Measure> measures = registry.getAvailableMeasures();
        HashSet<MeasureDescriptor> result =
                new HashSet<MeasureDescriptor>(measures.size());

        for (Measure measure : measures) {
            result.add(new MeasureDescriptor(measure));
        }

        for (Slave slave : slaves.values()) {
            result.addAll(slave.getMeasures());
        }
        return result;
    }

    public Slave getSlave(String url) {
        return slaves.get(url);
    }

    public Collection<Slave> getSlaves() {
        return slaves.values();
    }

    public void addSlave(String url) {
        if (!slaves.containsKey(url)) {
            slaves.put(url, new Slave(url));
        }
    }

    public ExecutionEngine getEngine() {
        return engine;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public CompareRegistry getRegistry() {
        return registry;
    }

    public static Injector getInjector() {
        return injector;
    }
}
