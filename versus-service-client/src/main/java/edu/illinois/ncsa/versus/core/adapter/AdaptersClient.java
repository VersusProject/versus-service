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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.concurrent.Callable;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import edu.illinois.ncsa.versus.core.ClientResourceFactory;
import edu.illinois.ncsa.versus.core.RetryService;

/**
 *
 * @author antoinev
 */
public class AdaptersClient {

    private static final String URL = "/adapters";

    private final String host;

    private final int getTimeout;

    private final int getRetry;

    public AdaptersClient(String host) {
        this(host, 10, 3);
    }

    public AdaptersClient(String host, int getTimeout, int getRetry) {
        this.host = host;
        this.getTimeout = getTimeout;
        this.getRetry = getRetry;
    }

    public HashSet<String> getAdapters() {
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

    public AdapterDescriptor getAdapterDescriptor(final String id) {
        RetryService<AdapterDescriptor> rs = new RetryService<AdapterDescriptor>(
                new Callable<AdapterDescriptor>() {
                    @Override
                    public AdapterDescriptor call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(
                                host + URL + '/' + id);
                        return cr.get(AdapterDescriptor.class);
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }

    public String getAdapterHelpSha1(final String adapterId) {
        RetryService<String> rs = new RetryService<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(
                                host + URL + '/' + adapterId + "/helpsha1");
                        return cr.get(String.class);
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }

    public InputStream getAdapterZippedHelp(final String adapterId) throws IOException {
        RetryService<InputStream> rs = new RetryService<InputStream>(
                new Callable<InputStream>() {
                    @Override
                    public InputStream call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(
                                host + URL + '/' + adapterId + "/help");
                        Representation representation = cr.get();
                        return representation.getStream();
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }
}
