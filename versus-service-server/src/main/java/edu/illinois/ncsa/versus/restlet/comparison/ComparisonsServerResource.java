package edu.illinois.ncsa.versus.restlet.comparison;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Form;
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
        for (Comparison comparison : comparisons) {
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

        HashMap<Integer, String> datasetsNames = new HashMap<Integer, String>();
        HashMap<Integer, InputStream> datasetsStreams =
                new HashMap<Integer, InputStream>();
        ArrayList<Integer> referenceDatasets = new ArrayList<Integer>();
        
        String adapter = null;
        String extractor = null;
        String measure = null;

        if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)) {
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

            for (FileItem item : items) {
                String name = item.getFieldName();
                if (name.equals("adapter")) {
                    adapter = item.getString();
                } else if (name.equals("extractor")) {
                    extractor = item.getString();
                } else if (name.equals("measure")) {
                    measure = item.getString();

                } else if (name.equals("referenceDatasets")) {
                    String ds = item.getString();
                    for (String dataset : ds.split(";")) {
                        Integer ref = new Integer(dataset);
                        referenceDatasets.add(ref);
                    }
                } else if (name.equals("datasetsUrls")) {
                    String ds = item.getString();
                    int i = 0;
                    for (String dataset : ds.split(";")) {
                        datasetsStreams.put(i, new URL(dataset).openStream());
                        datasetsNames.put(i, dataset);
                        i++;
                    }
                } else if (name.startsWith("datasetName")) {
                    Integer id = new Integer(name.substring(11));
                    datasetsNames.put(id, item.getString());
                } else if (name.startsWith("datasetStream")) {
                    Integer id = new Integer(name.substring(13));
                    datasetsStreams.put(id, item.getInputStream());
                } else {
                    getLogger().log(Level.INFO,
                            "Ignoring parameter ''{0}'' specified by client.", name);
                }
            }
        } else {
            Form form = new Form(entity);
            String ds = form.getFirstValue("datasetsUrls", null);
            int i = 0;
            for (String dataset : ds.split(";")) {
                datasetsStreams.put(i, new URL(dataset).openStream());
                datasetsNames.put(i, dataset);
                i++;
            }
            adapter = form.getFirstValue("adapter", null);
            extractor = form.getFirstValue("extractor", null);
            measure = form.getFirstValue("measure", null);
        }

        if (datasetsNames.isEmpty() || datasetsStreams.isEmpty()) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the input datasets.", MediaType.TEXT_PLAIN);
        }
        if (!CollectionUtils.isEqualCollection(datasetsNames.keySet(),
                datasetsStreams.keySet())) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Unconsistent input datasets.", MediaType.TEXT_PLAIN);
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
        for(Integer ref : referenceDatasets) {
            if(ref < 0 || ref >= datasetsNames.size()) {
                setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return new StringRepresentation("The reference dataset " + ref +
                        " cannot be found in the submitted datasets.", 
                        MediaType.TEXT_ALL);
            }
        }

        ArrayList<String> names = new ArrayList<String>(datasetsNames.size());
        ArrayList<InputStream> streams = new ArrayList<InputStream>(
                datasetsNames.size());
        TreeSet<Integer> keySet = new TreeSet<Integer>(datasetsNames.keySet());
        for (Integer key : keySet) {
            names.add(datasetsNames.get(key));
            streams.add(datasetsStreams.get(key));
        }

        List<String> comparisons;
        try {
            comparisons = submitComparisons(adapter, extractor, measure,
                    names, streams, referenceDatasets);
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
        String response = StringUtils.join(comparisons, ';');
        Representation representation = new StringRepresentation(
                response, MediaType.TEXT_PLAIN);
        return representation;
    }

    private List<String> submitComparisons(
            String adapter, String extractor, String measure,
            List<String> datasetsNames, List<InputStream> datasetsStreams,
            List<Integer> referenceDatasets)
            throws IOException, NoSlaveAvailableException {
        ServerApplication server = (ServerApplication) getApplication();
        ComparisonSubmitter submitter = new ComparisonSubmitter(server,
                adapter, extractor, measure, datasetsNames, datasetsStreams,
                referenceDatasets);
        return submitter.submit();
    }
}
