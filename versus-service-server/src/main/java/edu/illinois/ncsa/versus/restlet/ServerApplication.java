package edu.illinois.ncsa.versus.restlet;

import gov.nist.itl.ssd.similarity.test.execution.TestResultServerResource;
import edu.illinois.ncsa.versus.restlet.node.SlavesServerResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.restlet.routing.Variable;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.core.ClientResourceFactory;
import edu.illinois.ncsa.versus.core.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.core.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.core.measure.MeasureDescriptor;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.SlavesManager.SlaveQuery;
import edu.illinois.ncsa.versus.restlet.adapter.AdapterHelpServerResource;
import edu.illinois.ncsa.versus.restlet.adapter.AdapterHelpSha1ServerResource;
import edu.illinois.ncsa.versus.restlet.adapter.AdapterServerResource;
import edu.illinois.ncsa.versus.restlet.adapter.AdaptersServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonStatusServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonSupportServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonValueServerResource;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonsServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorHelpServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorHelpSha1ServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorServerResource;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorsServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasureHelpServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasureHelpSha1ServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasureServerResource;
import edu.illinois.ncsa.versus.restlet.measure.MeasuresServerResource;
import edu.illinois.ncsa.versus.restlet.node.MasterRegistrationService;
import edu.illinois.ncsa.versus.restlet.node.NodeStatusServerResource;
import edu.illinois.ncsa.versus.store.RepositoryModule;
import edu.illinois.ncsa.versus.utility.HasCategory;
import edu.illinois.ncsa.versus.utility.HasHelp;
import gov.nist.itl.ssd.sampling.SamplingExecutionEngine;
import gov.nist.itl.ssd.sampling.SamplingRegistry;
import gov.nist.itl.ssd.sampling.restlet.IndividualServerResource;
import gov.nist.itl.ssd.sampling.restlet.IndividualsServerResource;
import gov.nist.itl.ssd.sampling.restlet.SamplerServerResource;
import gov.nist.itl.ssd.sampling.restlet.SamplersServerResource;
import gov.nist.itl.ssd.sampling.restlet.SamplingServerResource;
import gov.nist.itl.ssd.sampling.restlet.SamplingsServerResource;
import gov.nist.itl.ssd.similarity.test.execution.TestServerResource;

/**
 * Main restlet application.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ServerApplication extends Application {

    private final CompareRegistry registry = new CompareRegistry();

    private final SamplingRegistry samplingRegistry = new SamplingRegistry();

    private final SlavesManager slavesManager = new SlavesManager();

    private final String masterURL;
    
    private final MasterRegistrationService masterRegistrationService;
    
    private final ExecutionEngine engine = new ExecutionEngine();

    private final SamplingExecutionEngine samplingEngine = new SamplingExecutionEngine();

    private final int port;

    private final String baseUrl;

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

        MasterRegistrationService mrs  = null;
        if (masterURL != null) {
            try {
                mrs = new MasterRegistrationService(port, baseUrl, masterUrl);
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
        }
        masterRegistrationService = mrs;

        ClientResourceFactory.setRetryDelay(500);
    }

    @Override
    public Restlet createInboundRoot() {

        Router router = new Router(getContext());
        router.attach(AdaptersServerResource.PATH_TEMPLATE, AdaptersServerResource.class);
        router.attach(AdapterServerResource.PATH_TEMPLATE, AdapterServerResource.class);
        router.attach(AdapterHelpServerResource.PATH_TEMPLATE, AdapterHelpServerResource.class);
        TemplateRoute adapterHelpRoute = router.attach(AdapterHelpServerResource.PATH_TEMPLATE + '/' + "{file}",
                AdapterHelpServerResource.class);
        adapterHelpRoute.getTemplate().getVariables().put(HelpServerResource.FILE_PARAMETER,
                new Variable(Variable.TYPE_ALL));
        router.attach(AdapterHelpSha1ServerResource.PATH_TEMPLATE, AdapterHelpSha1ServerResource.class);

        router.attach(ExtractorsServerResource.PATH_TEMPLATE, ExtractorsServerResource.class);
        router.attach(ExtractorServerResource.PATH_TEMPLATE, ExtractorServerResource.class);
        router.attach(ExtractorHelpServerResource.PATH_TEMPLATE, ExtractorHelpServerResource.class);
        TemplateRoute extractorHelpRoute = router.attach(ExtractorHelpServerResource.PATH_TEMPLATE + '/' + "{file}",
                ExtractorHelpServerResource.class);
        extractorHelpRoute.getTemplate().getVariables().put(HelpServerResource.FILE_PARAMETER,
                new Variable(Variable.TYPE_ALL));
        router.attach(ExtractorHelpSha1ServerResource.PATH_TEMPLATE, ExtractorHelpSha1ServerResource.class);

        router.attach(MeasuresServerResource.PATH_TEMPLATE, MeasuresServerResource.class);
        router.attach(MeasureServerResource.PATH_TEMPLATE, MeasureServerResource.class);
        router.attach(MeasureHelpServerResource.PATH_TEMPLATE, MeasureHelpServerResource.class);
        TemplateRoute measureHelpRoute = router.attach(MeasureHelpServerResource.PATH_TEMPLATE + '/' + "{file}",
                MeasureHelpServerResource.class);
        measureHelpRoute.getTemplate().getVariables().put(HelpServerResource.FILE_PARAMETER,
                new Variable(Variable.TYPE_ALL));
        router.attach(MeasureHelpSha1ServerResource.PATH_TEMPLATE, MeasureHelpSha1ServerResource.class);

        router.attach(SlavesServerResource.PATH_TEMPLATE, SlavesServerResource.class);

        router.attach(ComparisonsServerResource.PATH_TEMPLATE, ComparisonsServerResource.class);
        router.attach("/comparisons/{id}/status",
                ComparisonStatusServerResource.class);
        router.attach("/comparisons/{id}/value",
                ComparisonValueServerResource.class);
        router.attach(ComparisonServerResource.PATH_TEMPLATE, ComparisonServerResource.class);
        router.attach(ComparisonSupportServerResource.PATH_TEMPLATE, ComparisonSupportServerResource.class);


        router.attach(NodeStatusServerResource.PATH_TEMPLATE, NodeStatusServerResource.class);


        router.attach("/files/upload", UploadServerResource.class);
        router.attach("/files/{id}", FileServerResource.class);

        router.attach("/distributions", DistributionsServerResource.class);
        router.attach("/distributions/{id}", DistributionServerResource.class);

        router.attach("/decisionSupport", DecisionSupportsServerResource.class);
        router.attach("/decisionSupport/{id}", DecisionSupportServerResource.class);


        router.attach(IndividualsServerResource.PATH_TEMPLATE, IndividualsServerResource.class);
        router.attach(IndividualServerResource.PATH_TEMPLATE, IndividualServerResource.class);
        router.attach(SamplersServerResource.PATH_TEMPLATE, SamplersServerResource.class);
        router.attach(SamplerServerResource.PATH_TEMPLATE, SamplerServerResource.class);
        router.attach(SamplingsServerResource.PATH_TEMPLATE, SamplingsServerResource.class);
        router.attach(SamplingServerResource.PATH_TEMPLATE, SamplingServerResource.class);



        router.attach("/tests", TestServerResource.class);
        router.attach("/tests/{command}", TestServerResource.class);
        TemplateRoute testResultRoute = router.attach(
                "/testsresult" + '/' + "{file}", TestResultServerResource.class);
        testResultRoute.getTemplate().getVariables().put("file", new Variable(Variable.TYPE_ALL));

        router.attachDefault(DefaultServerResource.class);

        return router;
    }

    public AdapterDescriptor getAdapter(final String id)
            throws NotFoundException {
        Adapter adapter = CompareRegistry.getAdapter(id);
        if (adapter != null) {
            String category = adapter instanceof HasCategory
                    ? ((HasCategory) adapter).getCategory() : "";
            boolean hasHelp = adapter instanceof HasHelp;
            return new AdapterDescriptor(adapter.getName(),
                    adapter.getClass().getName(), category,
                    adapter.getSupportedMediaTypes(), hasHelp);
        }
        AdapterDescriptor ad = slavesManager.querySlavesFirstNotNull(
                new SlaveQuery<AdapterDescriptor>() {
                    @Override
                    public AdapterDescriptor executeQuery(Slave slave) {
                        return slave.getAdapter(id);
                    }
                });

        if (ad != null) {
            return ad;
        }
        throw new NotFoundException();
    }

    public HashSet<String> getAdaptersId() {
        Collection<String> adapters = registry.getAvailableAdaptersIds();
        HashSet<String> result = new HashSet<String>(adapters.size());

        for (String adapter : adapters) {
            result.add(adapter);
        }
        Collection<Set<String>> slavesResults = slavesManager.querySlaves(
                new SlaveQuery<Set<String>>() {
                    @Override
                    public Set<String> executeQuery(Slave slave) {
                        return slave.getAdaptersId();
                    }
                });
        for (Set<String> slaveResult : slavesResults) {
            result.addAll(slaveResult);
        }
        return result;
    }

    public String getAdapterHelpSha1(final String adapterId) throws NotFoundException {
        String sha1 = null;
        Adapter adapter = CompareRegistry.getAdapter(adapterId);
        if (adapter != null) {
            if (adapter instanceof HasHelp) {
                HasHelp hasHelp = (HasHelp) adapter;
                sha1 = hasHelp.getHelpSHA1();
            }
        } else {
            sha1 = slavesManager.querySlavesFirstNotNull(
                    new SlaveQuery<String>() {
                        @Override
                        public String executeQuery(Slave slave) {
                            return slave.getAdapterHelpSha1(adapterId);
                        }
                    });
        }
        if (sha1 == null) {
            throw new NotFoundException("The adapter " + adapterId + " has no help.");
        }
        return sha1;
    }

    public InputStream getAdapterZippedHelp(final String adapterId) throws NotFoundException {
        InputStream help = null;
        Adapter adapter = CompareRegistry.getAdapter(adapterId);
        if (adapter != null) {
            if (adapter instanceof HasHelp) {
                HasHelp hasHelp = (HasHelp) adapter;
                return hasHelp.getHelpZipped();
            }
        } else {
            help = slavesManager.querySlavesFirstNotNull(
                    new SlaveQuery<InputStream>() {
                        @Override
                        public InputStream executeQuery(Slave slave) {
                            try {
                                return slave.getAdapterZippedHelp(adapterId);
                            } catch (IOException ex) {
                                Logger.getLogger(ServerApplication.class.getName()).
                                        log(Level.WARNING, "IOException when querying slave " + slave, ex);
                                return null;
                            }
                        }
                    });
        }
        if (help == null) {
            throw new NotFoundException("The adapter " + adapterId + " has no help.");
        }
        return help;
    }

    public ExtractorDescriptor getExtractor(final String id)
            throws NotFoundException {
        Extractor extractor = CompareRegistry.getExtractor(id);
        if (extractor != null) {
            String category = extractor instanceof HasCategory
                    ? ((HasCategory) extractor).getCategory() : "";
            boolean hasHelp = extractor instanceof HasHelp;
            Collection<String> availableAdapters = registry.getAvailableAdaptersId(extractor);
            return new ExtractorDescriptor(extractor.getName(),
                    extractor.getClass().getName(), category,
                    availableAdapters, extractor.getFeatureType().getName(),
                    hasHelp);
        }

        ExtractorDescriptor ed = slavesManager.querySlavesFirstNotNull(
                new SlaveQuery<ExtractorDescriptor>() {
                    @Override
                    public ExtractorDescriptor executeQuery(Slave slave) {
                        return slave.getExtractor(id);
                    }
                });
        if (ed != null) {
            return ed;
        }
        throw new NotFoundException();
    }

    public HashSet<String> getExtractorsId() {
        Collection<String> extractors = registry.getAvailableExtractorsId();
        HashSet<String> result = new HashSet<String>(extractors.size());

        for (String extractor : extractors) {
            result.add(extractor);
        }
        Collection<Set<String>> slavesResult = slavesManager.querySlaves(
                new SlaveQuery<Set<String>>() {
                    @Override
                    public Set<String> executeQuery(Slave slave) {
                        return slave.getExtractorsId();
                    }
                });
        for (Set<String> slaveResult : slavesResult) {
            result.addAll(slaveResult);
        }
        return result;
    }

    public String getExtractorHelpSha1(final String extractorId) throws NotFoundException {
        String sha1 = null;
        Extractor extractor = CompareRegistry.getExtractor(extractorId);
        if (extractor != null) {
            if (extractor instanceof HasHelp) {
                HasHelp hasHelp = (HasHelp) extractor;
                sha1 = hasHelp.getHelpSHA1();
            }
        } else {
            sha1 = slavesManager.querySlavesFirstNotNull(
                    new SlaveQuery<String>() {
                        @Override
                        public String executeQuery(Slave slave) {
                            return slave.getExtractorHelpSha1(extractorId);
                        }
                    });
        }
        if (sha1 == null) {
            throw new NotFoundException("The extractor " + extractorId + " has no help.");
        }
        return sha1;
    }

    public InputStream getExtractorZippedHelp(final String extractorId) throws NotFoundException {
        InputStream help = null;
        Extractor extractor = registry.getExtractor(extractorId);
        if (extractor != null) {
            if (extractor instanceof HasHelp) {
                HasHelp hasHelp = (HasHelp) extractor;
                help = hasHelp.getHelpZipped();
            }
        } else {
            help = slavesManager.querySlavesFirstNotNull(
                    new SlaveQuery<InputStream>() {
                        @Override
                        public InputStream executeQuery(Slave slave) {
                            try {
                                return slave.getExtractorZippedHelp(extractorId);
                            } catch (IOException ex) {
                                Logger.getLogger(ServerApplication.class.getName()).
                                        log(Level.WARNING, "IOException when querying slave " + slave, ex);
                                return null;
                            }
                        }
                    });
        }
        if (help == null) {
            throw new NotFoundException("The extractor " + extractorId + " has no help.");
        }
        return help;
    }

    public MeasureDescriptor getMeasure(final String id)
            throws NotFoundException {
        Measure measure = registry.getMeasure(id);
        if (measure != null) {
            String category = measure instanceof HasCategory
                    ? ((HasCategory) measure).getCategory() : "";
            boolean hasHelp = measure instanceof HasHelp;
            Set<Class<? extends Descriptor>> supportedFeaturesTypes = measure.supportedFeaturesTypes();
            ArrayList<String> supportedFeatures = new ArrayList<String>(supportedFeaturesTypes.size());
            for (Class<? extends Descriptor> descriptorClass : supportedFeaturesTypes) {
                supportedFeatures.add(descriptorClass.getName());
            }
            return new MeasureDescriptor(measure.getName(), id, category, supportedFeatures, hasHelp);
        }
        MeasureDescriptor md = slavesManager.querySlavesFirstNotNull(
                new SlaveQuery<MeasureDescriptor>() {
                    @Override
                    public MeasureDescriptor executeQuery(Slave slave) {
                        return slave.getMeasure(id);
                    }
                });
        if (md != null) {
            return md;
        }
        throw new NotFoundException();
    }

    public HashSet<String> getMeasuresId() {
        Collection<String> measures = registry.getAvailableMeasuresId();
        HashSet<String> result = new HashSet<String>(measures.size());

        for (String measure : measures) {
            result.add(measure);
        }
        Collection<Set<String>> slavesResult = slavesManager.querySlaves(
                new SlaveQuery<Set<String>>() {
                    @Override
                    public Set<String> executeQuery(Slave slave) {
                        return slave.getMeasuresId();
                    }
                });
        for (Set<String> slaveResult : slavesResult) {
            result.addAll(slaveResult);
        }
        return result;
    }

    public String getMeasureHelpSha1(final String measureId) throws NotFoundException {
        String sha1 = null;
        Measure measure = registry.getMeasure(measureId);
        if (measure != null) {
            if (measure instanceof HasHelp) {
                HasHelp hasHelp = (HasHelp) measure;
                sha1 = hasHelp.getHelpSHA1();
            }
        } else {
            sha1 = slavesManager.querySlavesFirstNotNull(
                    new SlaveQuery<String>() {
                        @Override
                        public String executeQuery(Slave slave) {
                            return slave.getMeasureHelpSha1(measureId);
                        }
                    });
        }
        if (sha1 == null) {
            throw new NotFoundException("The measure " + measureId + " has no help.");
        }
        return sha1;
    }

    public InputStream getMeasureZippedHelp(final String measureId) throws NotFoundException {
        InputStream help = null;
        Measure measure = registry.getMeasure(measureId);
        if (measure != null) {
            if (measure instanceof HasHelp) {
                HasHelp hasHelp = (HasHelp) measure;
                help = hasHelp.getHelpZipped();
            }
        } else {
            help = slavesManager.querySlavesFirstNotNull(
                    new SlaveQuery<InputStream>() {
                        @Override
                        public InputStream executeQuery(Slave slave) {
                            try {
                                return slave.getMeasureZippedHelp(measureId);
                            } catch (IOException ex) {
                                Logger.getLogger(ServerApplication.class.getName()).
                                        log(Level.WARNING, "IOException when querying slave " + slave, ex);
                                return null;
                            }
                        }
                    });
        }
        if (help == null) {
            throw new NotFoundException("The measure " + measureId + " has no help.");
        }
        return help;
    }

    public SlavesManager getSlavesManager() {
        return slavesManager;
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

    public SamplingRegistry getSamplingRegistry() {
        return samplingRegistry;
    }

    public static Injector getInjector() {
        return injector;
    }

    public SamplingExecutionEngine getSamplingEngine() {
        return samplingEngine;
    }
}
