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
package edu.illinois.ncsa.versus.restlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import edu.illinois.ncsa.versus.restlet.adapter.AdapterHelpServerResource;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author antoinev
 */
public abstract class HelpServerResource extends VersusServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String FILE_PARAMETER = "file";

    private static final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

    private static final List<MediaType> supportedMediaTypes = new ArrayList<MediaType>(3);

    static {
        supportedMediaTypes.add(MediaType.APPLICATION_ZIP);
        supportedMediaTypes.add(MediaType.TEXT_ALL);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML);
    }
    
    protected abstract InputStream getZippedHelpStream(String id) throws NotFoundException;

    protected abstract String getType();

    @Get
    public Representation retrieve() throws IOException {
        ConcurrentMap<String, Object> attributes = getRequest().getAttributes();
        String id = (String) attributes.get(ID_PARAMETER);

        if (attributes.containsKey(FILE_PARAMETER)) {
            String file = (String) attributes.get(FILE_PARAMETER);
            return getFile(id, file);
        }

        MediaType preferredMediaType = getRequest().getClientInfo().getPreferredMediaType(supportedMediaTypes);
        if (MediaType.TEXT_ALL.equals(preferredMediaType)
                || MediaType.APPLICATION_XHTML.equals(preferredMediaType)) {
            redirectPermanent(getReference().toString() + "/index.html");
            return new StringRepresentation("Redirecting to html help.", MediaType.TEXT_PLAIN);
        }

        try {
            final InputStream stream = getZippedHelpStream(id);
            StreamRepresentation representation = new StreamRepresentation(MediaType.APPLICATION_ZIP) {

                @Override
                public InputStream getStream() {
                    return stream;
                }

                @Override
                public void write(OutputStream outputStream) throws IOException {
                    IOUtils.copy(stream, outputStream);
                }
            };

            Disposition disposition = new Disposition(Disposition.TYPE_ATTACHMENT);
            disposition.setFilename(id + "-help.zip");
            representation.setDisposition(disposition);
            return representation;
        } catch (NotFoundException ex) {
            Logger.getLogger(AdapterHelpServerResource.class.getName()).log(Level.SEVERE, null, ex);
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation(getType() + " help " + id + " not found", MediaType.TEXT_PLAIN);
        }
    }

    private Representation getFile(String id, String file) throws IOException {
        final InputStream stream;
        try {
            stream = getZippedHelpStream(id);
        } catch (NotFoundException ex) {
            Logger.getLogger(AdapterHelpServerResource.class.getName()).log(Level.SEVERE, null, ex);
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation(getType() + " help " + id + " not found", MediaType.TEXT_PLAIN);
        }
        final ZipInputStream zipStream = new ZipInputStream(stream);
        ZipEntry zipEntry = null;
        while ((zipEntry = zipStream.getNextEntry()) != null) {
            if (file.equalsIgnoreCase(zipEntry.getName())) {
                break;
            }
            zipStream.closeEntry();
        }
        if (zipEntry == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("File help " + file + " of " + getType() + ' ' + id + " not found", MediaType.TEXT_PLAIN);
        }
        String fileName = zipEntry.getName();
        String contentType = mimeTypesMap.getContentType(fileName);
        MediaType mediaType = new MediaType(contentType);

        StreamRepresentation representation = new StreamRepresentation(mediaType) {

            @Override
            public InputStream getStream() throws IOException {
                return zipStream;
            }

            @Override
            public void write(OutputStream outputStream) throws IOException {
                IOUtils.copy(zipStream, outputStream);
            }
        };
        return representation;
    }
}
