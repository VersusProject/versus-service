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
package edu.illinois.ncsa.versus.core.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import edu.illinois.ncsa.versus.core.ClientResourceFactory;

/**
 *
 * @author antoinev
 */
public class ExtractorsClient {

    private static final String URL = "/extractors";

    private final String host;

    public ExtractorsClient(String host) {
        this.host = host;
    }

    public HashSet<String> getExtractors() {
        ClientResource cr = ClientResourceFactory.getNew(host + URL);
        return cr.get(HashSet.class);
    }

    public ExtractorDescriptor getExtractorDescriptor(String id) {
        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + id);
        return cr.get(ExtractorDescriptor.class);
    }

    public String getAdapterHelpSha1(String extractorId) {
        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + extractorId + "/helpsha1");
        return cr.get(String.class);
    }

    public String getExtractorHelpSha1(String extractorId) {
        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + extractorId + "/helpsha1");
        return cr.get(String.class);
    }

    public InputStream getExtractorZippedHelp(String extractorId) throws IOException {
        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + extractorId + "/help");
        Representation representation = cr.get();
        return representation.getStream();
    }
}
