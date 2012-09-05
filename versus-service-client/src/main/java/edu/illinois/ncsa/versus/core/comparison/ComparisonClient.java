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
package edu.illinois.ncsa.versus.core.comparison;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
public class ComparisonClient {

    private static final String URL = "/comparisons";

    private final String host;

    public ComparisonClient(String host) {
        this.host = host;
    }

    public Comparison getComparison(String id) {
        ClientResource cr = new ClientResource(host + URL + '/' + id);
        return cr.get(Comparison.class);
    }

    public HashSet<String> getComparisons() {
        ClientResource cr = new ClientResource(host + URL);
        return cr.get(HashSet.class);
    }

    public String submit(Comparison comparison) throws IOException {
        URL url1 = new URL(comparison.getFirstDataset());
        URL url2 = new URL(comparison.getSecondDataset());
        return submit(comparison, url1.openStream(), url2.openStream());
    }

    public String submit(Comparison comparison, InputStream dataset1Stream, InputStream dataset2Stream) throws IOException {
        return submit(comparison.getAdapterId(), comparison.getExtractorId(), 
                comparison.getMeasureId(), 
                comparison.getFirstDataset(), comparison.getSecondDataset(),
                dataset1Stream, dataset2Stream);
    }

    public String submit(String adapterId, String extractorId, String measureId, File file1, File file2) throws IOException {
        if (!file1.exists()) {
            throw new FileNotFoundException("File " + file1 + " not found.");
        }
        if (!file2.exists()) {
            throw new FileNotFoundException("File " + file2 + " not found.");
        }
        return submit(adapterId, extractorId, measureId,
                file1.getName(), file2.getName(),
                new FileInputStream(file1), new FileInputStream(file2));
    }

    public List<String> submit(String adapterId, String extractorId, String measureId,
            List<String> datasetsNames, List<InputStream> datasetsStreams,
            List<Integer> referenceDatasets) throws IOException {
        FormDataSet form = new FormDataSet();
        form.setMultipart(true);
        Series<FormData> entries = form.getEntries();
        entries.add(new FormData("adapter", adapterId));
        entries.add(new FormData("extractor", extractorId));
        entries.add(new FormData("measure", measureId));
        int i = 0;
        Iterator<String> namesIt = datasetsNames.iterator();
        Iterator<InputStream> streamsIt = datasetsStreams.iterator();
        while (namesIt.hasNext() && streamsIt.hasNext()) {
            entries.add(new FormData("datasetName" + i, namesIt.next()));
            entries.add(new FormData("datasetStream" + i,
                    new InputRepresentation(streamsIt.next(), MediaType.ALL)));
            i++;
        }
        if (referenceDatasets != null && !referenceDatasets.isEmpty()) {
            String join = StringUtils.join(referenceDatasets, ';');
            entries.add(new FormData("referenceDatasets", join));
        }


        ClientResource clientResource = new ClientResource(host + URL);
        Representation post = clientResource.post(form);
        String response = post.getText();

        String[] split = StringUtils.split(response, ';');
        return Arrays.asList(split);
    }

    public String submit(String adapterId, String extractorId, String measureId,
            String dataset1Name, String dataset2Name,
            InputStream dataset1Stream, InputStream dataset2Stream)
            throws IOException {
        ArrayList<String> names = new ArrayList<String>(2);
        names.add(dataset1Name);
        names.add(dataset2Name);
        ArrayList<InputStream> streams = new ArrayList<InputStream>(2);
        streams.add(dataset1Stream);
        streams.add(dataset2Stream);
        return submit(adapterId, extractorId, measureId, names, streams, null)
                .get(0);
    }

    public boolean supportComparison(String adapterId, String extractorId, String measureId) {
        ClientResource cr = new ClientResource(host + URL + '/'
                + adapterId + '/' + extractorId + '/' + measureId);
        return cr.get(Boolean.class);
    }
}
