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
package edu.illinois.ncsa.versus.core.node;

import java.util.HashSet;
import java.util.concurrent.Callable;

import org.restlet.resource.ClientResource;

import edu.illinois.ncsa.versus.core.ClientResourceFactory;
import edu.illinois.ncsa.versus.core.RetryService;

/**
 *
 * @author antoinev
 */
public class SlavesClient {

    private static final String URL = "/slaves";

    private final String host;

    private final int getTimeout;

    private final int getRetry;

    public SlavesClient(String host, int getTimeout, int getRetry) {
        this.host = host;
        this.getTimeout = getTimeout;
        this.getRetry = getRetry;
    }

    public HashSet<String> getSlaves() {
        RetryService<HashSet<String>> rs = new RetryService<HashSet<String>>(
                new Callable<HashSet<String>>() {
                    @Override
                    public HashSet<String> call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(host + URL);
                        return cr.get(HashSet.class);
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }
}
