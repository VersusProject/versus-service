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
package gov.nist.itl.ssd.sampling;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.restlet.data.MediaType;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

import edu.illinois.ncsa.versus.core.ClientResourceFactory;
import edu.illinois.ncsa.versus.core.RetryService;

/**
 *
 * @author antoinev
 */
public class SamplingClient {

    private static final String URL = "/samplings";

    private final String host;

    private final int postTimeout;

    private final int postRetry;

    private final int getTimeout;

    private final int getRetry;

    public SamplingClient(String host) {
        this(host, 60, 3, 10, 3);
    }

    public SamplingClient(String host, int postTimeout, int postRetry,
            int getTimeout, int getRetry) {
        this.host = host;
        this.postTimeout = postTimeout;
        this.postRetry = postRetry;
        this.getTimeout = getTimeout;
        this.getRetry = getRetry;
    }

    public Sampling getSampling(final String id) {
        RetryService<Sampling> rs = new RetryService<Sampling>(
                new Callable<Sampling>() {
                    @Override
                    public Sampling call() throws Exception {
                        ClientResource cr = ClientResourceFactory.getNew(host + URL + '/' + id);
                        return cr.get(Sampling.class);
                    }
                }, getTimeout, getRetry);
        return rs.run();
    }

    public HashSet<String> getSamplings() {
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

    public String submit(Sampling sampling, List<InputStream> datasets)
            throws IOException {
        return submit(sampling.getIndividual(), sampling.getSampler(),
                sampling.getSampleSize(), sampling.getDatasets(), datasets);
    }

    public String submit(String individualId, String samplerId, int sampleSize,
            List<String> datasetsNames, List<InputStream> datasetsStreams)
            throws IOException {
        final FormDataSet form = new FormDataSet();
        form.setMultipart(true);
        Series<FormData> entries = form.getEntries();
        entries.add(new FormData("individual", individualId));
        entries.add(new FormData("sampler", samplerId));
        entries.add(new FormData("sampleSize", Integer.toString(sampleSize)));
        int i = 0;
        Iterator<String> namesIt = datasetsNames.iterator();
        Iterator<InputStream> streamsIt = datasetsStreams.iterator();
        while (namesIt.hasNext() && streamsIt.hasNext()) {
            entries.add(new FormData("datasetUrl" + i, namesIt.next()));
            entries.add(new FormData("datasetStream" + i,
                    new InputRepresentation(streamsIt.next(), MediaType.ALL)));
            i++;
        }

        RetryService<Representation> rs = new RetryService<Representation>(
                new Callable<Representation>() {
                    @Override
                    public Representation call() throws Exception {
                        ClientResource cr =  ClientResourceFactory.getNew(host + URL);
                        return cr.post(form);
                    }
                }, postTimeout, postRetry);
        Representation post = rs.run();
        return post.getText();
    }
}
