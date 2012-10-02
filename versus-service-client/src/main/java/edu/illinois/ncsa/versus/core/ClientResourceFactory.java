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
package edu.illinois.ncsa.versus.core;

import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

/**
 *
 * @author antoinev
 */
public class ClientResourceFactory {

    private volatile static long retryDelay = 2000;
    
    private static final Client client = new Client(Protocol.HTTP);

    private ClientResourceFactory() {
    }

    public static ClientResource getNew(String url) {
        ClientResource cr = new ClientResource(url);
        cr.setRetryDelay(retryDelay);
        cr.setNext(client);
        return cr;
    }

    public static long getRetryDelay() {
        return retryDelay;
    }

    public static void setRetryDelay(long retryDelay) {
        ClientResourceFactory.retryDelay = retryDelay;
    }
}
