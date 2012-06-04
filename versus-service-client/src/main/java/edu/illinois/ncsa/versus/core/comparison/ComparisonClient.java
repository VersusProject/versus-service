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
import java.util.HashSet;

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
        return submit(comparison.getAdapterId(), comparison.getExtractorId(), comparison.getMeasureId(), dataset1Stream, dataset2Stream);
    }
    
    public String submit(String adapterId, String extractorId, String measureId, File file1, File file2) throws IOException {
        if(!file1.exists()) {
            throw new FileNotFoundException("File " + file1 + " not found.");
        }
        if(!file2.exists()) {
            throw new FileNotFoundException("File " + file2 + " not found.");
        }
        return submit(adapterId, extractorId, measureId, new FileInputStream(file1), new FileInputStream(file2));
    }
    
    public String submit(String adapterId, String extractorId, String measureId, InputStream dataset1, InputStream dataset2) throws IOException {
        FormDataSet form = new FormDataSet();
        form.setMultipart(true);
        Series<FormData> entries = form.getEntries();
        entries.add(new FormData("adapter", adapterId));
        entries.add(new FormData("extractor", extractorId));
        entries.add(new FormData("measure", measureId));
        entries.add(new FormData("dataset1", new InputRepresentation(dataset1, MediaType.ALL)));
        entries.add(new FormData("dataset2", new InputRepresentation(dataset2, MediaType.ALL)));
        
        ClientResource clientResource = new ClientResource(host + URL);
        Representation post = clientResource.post(form);
        return post.getText();
    }

    public boolean supportComparison(String adapterId, String extractorId, String measureId) {
        ClientResource cr = new ClientResource(host + URL + '/'
                + adapterId + '/' + extractorId + '/' + measureId);
        return cr.get(Boolean.class);
    }
}
