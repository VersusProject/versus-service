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

import org.restlet.data.MediaType;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

/**
 *
 * @author antoinev
 */
public class SamplingClient {

    private static final String URL = "/samplings";

    private final String host;

    public SamplingClient(String host) {
        this.host = host;
    }

    public Sampling getSampling(String id) {
        ClientResource cr = new ClientResource(host + URL + '/' + id);
        return cr.get(Sampling.class);
    }

    public HashSet<String> getSamplings() {
        ClientResource cr = new ClientResource(host + URL);
        return cr.get(HashSet.class);
    }

    public String submit(Sampling sampling, List<InputStream> datasets)
            throws IOException {
        return submit(sampling.getIndividual(), sampling.getSampler(),
                sampling.getSampleSize(), sampling.getDatasets(), datasets);
    }

    public String submit(String individualId, String samplerId, int sampleSize,
            List<String> datasetsNames, List<InputStream> datasetsStreams)
            throws IOException {
        FormDataSet form = new FormDataSet();
        form.setMultipart(true);
        Series<FormData> entries = form.getEntries();
        entries.add(new FormData("individual", individualId));
        entries.add(new FormData("sampler", samplerId));
        entries.add(new FormData("sampleSize", Integer.toString(sampleSize)));
        int i = 0;
        Iterator<String> namesIt = datasetsNames.iterator();
        Iterator<InputStream> streamsIt = datasetsStreams.iterator();
        while(namesIt.hasNext() && streamsIt.hasNext()) {
            entries.add(new FormData("datasetUrl" + i, namesIt.next()));
            entries.add(new FormData("datasetStream" + i,
                    new InputRepresentation(streamsIt.next(), MediaType.ALL)));
            i++;
        }
        ClientResource cr = new ClientResource(host + URL);
        Representation post = cr.post(form);
        return post.getText();
    }
}
