package edu.illinois.ncsa.versus.restlet.comparison;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.ComparisonStatusHandler;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.Job;
import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.RankSlaves;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.Slave;
import edu.illinois.ncsa.versus.restlet.SlavesManager;
import edu.illinois.ncsa.versus.restlet.StringCollectionConverter;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Multiple comparisons.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ComparisonsServerResource extends ServerResource {

    public static final String URL = "/comparisons";

    public static final String PATH_TEMPLATE = URL;

    @Get()
    public HashSet<String> retrieve() {
        // Guice storage
        Injector injector = Guice.createInjector(new RepositoryModule());
        ComparisonServiceImpl comparisonService =
                injector.getInstance(ComparisonServiceImpl.class);
        Collection<Comparison> comparisons = comparisonService.listAll();
        HashSet<String> result = new HashSet<String>(comparisons.size());
        for (Comparison comparison : comparisonService.listAll()) {
            result.add(comparison.getId());
        }
        return result;
    }
    
    @Get("xml")
    public String asXml() {
        XStream xstream = new XStream();
        return fillAndConvert(xstream);
    }

    @Get("json")
    public String asJson() {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        return fillAndConvert(xstream);
    }

    private String fillAndConvert(XStream xstream) {
        xstream.alias("comparisons", HashSet.class);
        xstream.registerConverter(
                new StringCollectionConverter<HashSet<String>>() {

                    @Override
                    protected String getNodeName() {
                        return "comparison";
                    }

                    @Override
                    protected HashSet<String> getNewT() {
                        return new HashSet<String>();
                    }

                    @Override
                    public boolean canConvert(Class type) {
                        return HashSet.class.isAssignableFrom(type);
                    }
                });

        return xstream.toXML(retrieve());
    }

    /**
     * List of comparisons.
     *
     * @return
     */
    @Get("html")
    public Representation asHtml() {
        Collection<String> comparisons = retrieve();
        if (comparisons.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No comparisons", MediaType.TEXT_HTML);
            return representation;
        } else {
            ServerApplication server = (ServerApplication) getApplication();
            String baseUrl = server.getBaseUrl();
            StringBuilder content =
                    new StringBuilder("<h3>Versus > Comparisons</h3><ul>");
            for (String id : comparisons) {
                content.append("<li><a href='").append(baseUrl).
                        append(ComparisonServerResource.URL).append(id).
                        append("'>").append(id).append("</a></li>");
            }
            content.append("</ul>");
            Representation representation = new StringRepresentation(content,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }

    @Post
    public Comparison submit(Comparison comparison) {
        try {
            comparison = submitComparison(comparison);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
            setStatus(Status.SERVER_ERROR_INTERNAL);
            return null;
        } catch (NoSlaveAvailableException ex) {
            getLogger().log(Level.SEVERE, null, ex);
            setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
            return null;
        }
        setStatus(Status.SUCCESS_CREATED);
        return comparison;
    }

    @Post("form:txt")
    public Representation submit(Form form) {
        if (form == null) {
            getLogger().log(Level.INFO, "No comparison specified by client.");
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify parameters", MediaType.TEXT_PLAIN);
        }

        Comparison comparison = new Comparison(
                form.getFirstValue("dataset1"),
                form.getFirstValue("dataset2"),
                form.getFirstValue("adapter"),
                form.getFirstValue("extractor"),
                form.getFirstValue("measure"));

        try {
            comparison = submitComparison(comparison);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
            setStatus(Status.SERVER_ERROR_INTERNAL);
            return new StringRepresentation(
                    "Cannot acces submitted datasets.", MediaType.TEXT_PLAIN);
        } catch (NoSlaveAvailableException ex) {
            getLogger().log(Level.SEVERE, null, ex);
            setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
            return new StringRepresentation(
                    "No slaves are available that can handle this comparison",
                    MediaType.TEXT_PLAIN);
        }
        setStatus(Status.SUCCESS_CREATED);
        String comparisonURL = getRequest().getResourceRef().getIdentifier()
                + "/" + comparison.getId();
        Representation representation = new StringRepresentation(comparisonURL,
                MediaType.TEXT_PLAIN);
        representation.setLocationRef(comparisonURL);
        return representation;
    }

    private Comparison submitComparison(Comparison comparison)
            throws IOException, NoSlaveAvailableException {
        ServerApplication server = (ServerApplication) getApplication();
        ComparisonSubmitter submitter = new ComparisonSubmitter(server, comparison);
        return submitter.submit();
    }
}
