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
package edu.illinois.ncsa.versus.restlet.adapter;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 *
 * @author antoinev
 */
public class AdaptersClient {

    private String host;

    public AdaptersClient(String host) {
        this.host = host;
    }

    public HashSet<AdapterDescriptor> getAdapters() {
        ClientResource cr = new ClientResource(host + AdaptersServerResource.URL);
        try {
            Representation jsonRepresentation = cr.get(MediaType.APPLICATION_JSON);
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            xstream.processAnnotations(AdapterDescriptor.class);
            return (HashSet<AdapterDescriptor>) xstream.fromXML(
                    jsonRepresentation.getStream());
        } catch (IOException ex) {
            Logger.getLogger(AdaptersClient.class.getName()).log(Level.WARNING,
                    "Cannot get adapters list from host " + host, ex);
            return new HashSet<AdapterDescriptor>();
        }
    }
}
