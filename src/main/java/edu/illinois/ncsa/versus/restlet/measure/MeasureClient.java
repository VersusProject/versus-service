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
package edu.illinois.ncsa.versus.restlet.measure;

import org.restlet.resource.ClientResource;

/**
 *
 * @author antoinev
 */
public class MeasureClient {

    private String host;

    public MeasureClient(String host) {
        this.host = host;
    }

    public MeasureDescriptor getMeasureDescriptor(String id) {
        ClientResource cr = new ClientResource(host + MeasureServerResource.URL + id);
        return cr.get(MeasureDescriptor.class);
    }
}
