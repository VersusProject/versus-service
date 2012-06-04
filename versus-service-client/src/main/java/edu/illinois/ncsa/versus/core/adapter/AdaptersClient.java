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
package edu.illinois.ncsa.versus.core.adapter;

import java.util.HashSet;

import org.restlet.resource.ClientResource;

import edu.illinois.ncsa.versus.core.ClientResourceFactory;

/**
 *
 * @author antoinev
 */
public class AdaptersClient {

    private static final String URL = "/adapters";

    private final String host;

    public AdaptersClient(String host) {
        this.host = host;
    }

    public HashSet<String> getAdapters() {
        ClientResource cr = ClientResourceFactory.getNew(host + URL);
        return cr.get(HashSet.class);
    }

    public AdapterDescriptor getAdapterDescriptor(String id) {
        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + id);
        return cr.get(AdapterDescriptor.class);
    }
}
