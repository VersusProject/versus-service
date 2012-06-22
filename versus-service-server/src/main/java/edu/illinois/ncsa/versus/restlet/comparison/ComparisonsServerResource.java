package edu.illinois.ncsa.versus.restlet.comparison;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.core.StringCollectionConverter;
import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 * Multiple comparisons.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ComparisonsServerResource extends VersusServerResource {

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

    @Post()
    public Representation submit(Representation entity) throws IOException {
        if (entity == null) {
            getLogger().log(Level.INFO, "No comparison specified by client.");
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify parameters", MediaType.TEXT_PLAIN);
        }

        if (!MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)) {
            getLogger().log(Level.INFO, "Unexpected media type specified by client.");
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Submit a multipart form/data.", MediaType.TEXT_PLAIN);
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1000240);
        RestletFileUpload upload = new RestletFileUpload(factory);
        List<FileItem> items;
        try {
            items = upload.parseRequest(getRequest());
        } catch (FileUploadException ex) {
            getLogger().log(Level.INFO, "Cannot parse client request", ex);
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Invalid request: " + ex.getMessage(), MediaType.TEXT_PLAIN);
        }

        String dataset1Name = null;
        String dataset2Name = null;
        InputStream dataset1Stream = null;
        InputStream dataset2Stream = null;
        String adapter = null;
        String extractor = null;
        String measure = null;
        for (FileItem item : items) {
            String name = item.getFieldName();
            if (name.equals("dataset1")) {
                dataset1Name = item.getName();
                dataset1Stream = item.getInputStream();
            } else if (name.equals("dataset2")) {
                dataset2Name = item.getName();
                dataset2Stream = item.getInputStream();
            } else if (name.equals("dataset1Url")) {
                dataset1Name = item.getString();
                dataset1Stream = new URL(dataset1Name).openStream();
            } else if (name.equals("dataset2Url")) {
                dataset2Name = item.getString();
                dataset2Stream = new URL(dataset1Name).openStream();
            } else if (name.equals("adapter")) {
                adapter = item.getString();
            } else if (name.equals("extractor")) {
                extractor = item.getString();
            } else if (name.equals("measure")) {
                measure = item.getString();
            } else {
                getLogger().log(Level.INFO,
                        "Ignoring parameter ''{0}'' specified by client.", name);
            }
        }

        if (dataset1Name == null || dataset1Stream == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the first dataset.", MediaType.TEXT_PLAIN);
        }
        if (dataset2Name == null || dataset2Stream == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the second dataset.", MediaType.TEXT_PLAIN);
        }
        if (adapter == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the adapter.", MediaType.TEXT_PLAIN);
        }
        if (extractor == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the extractor.", MediaType.TEXT_PLAIN);
        }
        if (measure == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the measure.", MediaType.TEXT_PLAIN);
        }

        Comparison comparison = new Comparison(dataset1Name, dataset2Name,
                adapter, extractor, measure);

        try {
            comparison = submitComparison(comparison, dataset1Stream, dataset2Stream);
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
        Representation representation = new StringRepresentation(
                comparison.getId(), MediaType.TEXT_PLAIN);
        representation.setLocationRef(comparisonURL);
        return representation;
    }

    private Comparison submitComparison(Comparison comparison, InputStream dataset1Stream, InputStream dataset2Stream)
            throws IOException, NoSlaveAvailableException {
        ServerApplication server = (ServerApplication) getApplication();
        ComparisonSubmitter submitter = new ComparisonSubmitter(server, comparison, dataset1Stream, dataset2Stream);
        return submitter.submit();
    }
}
