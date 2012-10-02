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
package gov.nist.itl.ssd.similarity.test.execution;

import java.io.File;
import java.util.concurrent.ConcurrentMap;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import edu.illinois.ncsa.versus.restlet.VersusServerResource;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author antoinev
 */
public class TestResultServerResource extends VersusServerResource {

    private static final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

    @Get
    public Representation retrieve() {
        ConcurrentMap<String, Object> attributes = getRequest().getAttributes();

        String fileName = (String) attributes.get("file");
        if(fileName == null) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("The file does not exist.");
        }

        File file = new File(fileName);
        if (!file.exists()) {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return new StringRepresentation("The file does not exist.");
        }
        String contentType = mimeTypesMap.getContentType(file);
        MediaType mediaType = new MediaType(contentType);
        return new FileRepresentation(file, mediaType);
    }
}
