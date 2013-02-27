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
package edu.illinois.ncsa.versus.core.measure;

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
public class MeasuresClient {

    private static final String URL = "/measures";

    private final String host;

    private final int getTimeout;

    private final int getRetry;

    public MeasuresClient(String host) {
        this(host, 10, 3);
    }

    public MeasuresClient(String host, int getTimeout, int getRetry) {
        this.host = host;
        this.getTimeout = getTimeout;
        this.getRetry = getRetry;
    }

    public HashSet<String> getMeasures() {
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

    public MeasureDescriptor getMeasureDescriptor(final String id) {
        RetryService<MeasureDescriptor> rs = new RetryService<MeasureDescriptor>(
                new Callable<MeasureDescriptor>() {
                    @Override
                    public MeasureDescriptor call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(
                                host + URL + '/' + id);
                        return cr.get(MeasureDescriptor.class);
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }

    public String getMeasureHelpSha1(final String measureId) {
        RetryService<String> rs = new RetryService<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(
                                host + URL + '/' + measureId + "/helpsha1");
                        return cr.get(String.class);
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }

    public InputStream getMeasureZippedHelp(final String measureId) throws IOException {
        RetryService<InputStream> rs = new RetryService<InputStream>(
                new Callable<InputStream>() {
                    @Override
                    public InputStream call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + measureId + "/help");
                        Representation representation = cr.get();
                        return representation.getStream();
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }
}
