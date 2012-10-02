/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package gov.nist.itl.ssd.sampling.restlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
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

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.core.StringCollectionConverter;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import gov.nist.itl.ssd.sampling.Sampling;
import gov.nist.itl.ssd.versus.store.SamplingService;

/**
 *
 * @author antoinev
 */
public class SamplingsServerResource extends VersusServerResource {

    public static final String URL = "/samplings";

    public static final String PATH_TEMPLATE = URL;

    public HashSet<String> retrieve() {
        Injector injector = ServerApplication.getInjector();
        SamplingService samplingService =
                injector.getInstance(SamplingService.class);
        Collection<Sampling> samplings = samplingService.listAll();
        HashSet<String> result = new HashSet<String>(samplings.size());
        for (Sampling sampling : samplings) {
            result.add(sampling.getId());
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
        xstream.alias("samplings", HashSet.class);
        xstream.registerConverter(
                new StringCollectionConverter<HashSet<String>>() {

                    @Override
                    protected String getNodeName() {
                        return "sampling";
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
     * List of samplings.
     *
     * @return
     */
    @Get("html")
    public Representation asHtml() {
        Collection<String> samplings = retrieve();
        if (samplings.isEmpty()) {
            Representation representation = new StringRepresentation(
                    "No samplings", MediaType.TEXT_HTML);
            return representation;
        } else {
            ServerApplication server = (ServerApplication) getApplication();
            String baseUrl = server.getBaseUrl();
            StringBuilder content =
                    new StringBuilder("<h3>Versus > Samplings</h3><ul>");
            for (String id : samplings) {
                content.append("<li><a href='").append(baseUrl).
                        append("/samplings/").append(id).
                        append("'>").append(id).append("</a></li>");
            }
            content.append("</ul>");
            Representation representation = new StringRepresentation(content,
                    MediaType.TEXT_HTML);
            return representation;
        }
    }

    @Post
    public Representation submit(Representation entity) throws IOException {
        if (entity == null) {
            getLogger().log(Level.INFO, "No comparison specified by client.");
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify parameters", MediaType.TEXT_PLAIN);
        }

        HashMap<String, String> datasetsNames = new HashMap<String, String>();
        HashMap<String, InputStream> datasetsStreams =
                new HashMap<String, InputStream>();
        String individual = null;
        String sampler = null;
        String stringSampleSize = null;
        Integer sampleSize;

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
                if (name.equals("individual")) {
                    individual = item.getString();
                } else if (name.equals("sampler")) {
                    sampler = item.getString();
                } else if (name.equals("sampleSize")) {
                    stringSampleSize = item.getString();
                } else if (name.equals("datasetsUrls")) {
                    String ds = item.getString();
                    int i = 0;
                    for (String dataset : ds.split(";")) {
                        datasetsStreams.put(Integer.toString(i),
                                new URL(dataset).openStream());
                        datasetsNames.put(Integer.toString(i), dataset);
                        i++;
                    }
                } else if (name.startsWith("datasetUrl")) {
                    datasetsNames.put(name.substring(10), item.getString());
                } else if (name.startsWith("datasetStream")) {
                    datasetsStreams.put(name.substring(13), item.getInputStream());
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
                datasetsStreams.put(Integer.toString(i),
                        new URL(dataset).openStream());
                datasetsNames.put(Integer.toString(i), dataset);
                i++;
            }
            individual = form.getFirstValue("individual");
            sampler = form.getFirstValue("sampler");
            stringSampleSize = form.getFirstValue("sampleSize");
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
        if (individual == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the individual type.", MediaType.TEXT_PLAIN);
        }
        if (sampler == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the sampler.", MediaType.TEXT_PLAIN);
        }
        try {
            sampleSize = Integer.parseInt(stringSampleSize);
        } catch (NumberFormatException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Specify the sample size in an integer format.", MediaType.TEXT_PLAIN);
        }

        String[] names = new String[datasetsNames.size()];
        InputStream[] streams = new InputStream[datasetsNames.size()];
        for (String key : datasetsNames.keySet()) {
            int i = Integer.parseInt(key);
            names[i] = datasetsNames.get(key);
            streams[i] = datasetsStreams.get(key);
        }

        ServerApplication server = (ServerApplication) getApplication();
        Sampling sampling = new Sampling(individual, sampler, sampleSize, Arrays.asList(names));
        SamplingSubmitter submitter = new SamplingSubmitter(server, sampling, Arrays.asList(streams));
        try {
            sampling = submitter.submit();
        } catch (NoSlaveAvailableException ex) {
            Logger.getLogger(SamplingsServerResource.class.getName()).
                    log(Level.SEVERE, null, ex);
            setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
            return new StringRepresentation(
                    "No slaves are available that can handle this sampling",
                    MediaType.TEXT_PLAIN);
        }
        setStatus(Status.SUCCESS_CREATED);
        String samplingURL = getRequest().getResourceRef().getIdentifier()
                + "/" + sampling.getId();
        Representation representation = new StringRepresentation(
                sampling.getId(), MediaType.TEXT_PLAIN);
        representation.setLocationRef(samplingURL);
        return representation;
    }
}
