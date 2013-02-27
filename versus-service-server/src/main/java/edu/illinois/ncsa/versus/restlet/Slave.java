/**
 *
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.resource.ClientResource;

import edu.illinois.ncsa.versus.core.ClientResourceFactory;
import edu.illinois.ncsa.versus.core.RetryService;
import edu.illinois.ncsa.versus.core.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.core.adapter.AdaptersClient;
import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.ComparisonClient;
import edu.illinois.ncsa.versus.core.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.core.extractor.ExtractorsClient;
import edu.illinois.ncsa.versus.core.measure.MeasureDescriptor;
import edu.illinois.ncsa.versus.core.measure.MeasuresClient;
import edu.illinois.ncsa.versus.restlet.node.NodeStatusServerResource;

/**
 * @author lmarini
 *
 */
public class Slave {

    private final String url;

    private final ComparisonClient comparisonClient;

    private final AdaptersClient adaptersClient;

    private final ExtractorsClient extractorsClient;

    private final MeasuresClient measuresClient;

    private final ClientResource nodeStatusClient;

    private final int postTimeout;

    private final int postRetry;

    private final int getTimeout;

    private final int getRetry;

    public Slave(String hostRef) {
        int postTimeout = 60;
        int postRetry = 3;
        int getTimeout = 10;
        int getRetry = 3;
        Properties properties;
        try {
            properties = PropertiesUtil.load();
            String postTimeoutString = properties.getProperty("slave.post.timeout", "60");
            String postRetryString = properties.getProperty("slave.post.retry", "3");
            String getTimeoutString = properties.getProperty("slave.get.timeout", "10");
            String getRetryString = properties.getProperty("slave.get.retry", "3");
            postTimeout = Integer.parseInt(postTimeoutString);
            postRetry = Integer.parseInt(postRetryString);
            getTimeout = Integer.parseInt(getTimeoutString);
            getRetry = Integer.parseInt(getRetryString);
        } catch (Exception ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.WARNING,
                    "Cannot read timeout and retry properties. Using default values.", ex);
        }

        url = hostRef;
        adaptersClient = new AdaptersClient(url);
        extractorsClient = new ExtractorsClient(url);
        measuresClient = new MeasuresClient(url);
        comparisonClient = new ComparisonClient(url, postTimeout, postRetry, getTimeout, getRetry);
        nodeStatusClient = ClientResourceFactory.getNew(
                url + NodeStatusServerResource.URL);
        this.postTimeout = postTimeout;
        this.postRetry = postRetry;
        this.getTimeout = getTimeout;
        this.getRetry = getRetry;
    }

    public String getUrl() {
        return url;
    }

    public AdapterDescriptor getAdapter(String id) {
        return adaptersClient.getAdapterDescriptor(id);
    }

    public Set<String> getAdaptersId() {
        return adaptersClient.getAdapters();
    }

    public String getAdapterHelpSha1(String adapterId) {
        return adaptersClient.getAdapterHelpSha1(adapterId);
    }

    public InputStream getAdapterZippedHelp(String adapterId) throws IOException {
        return adaptersClient.getAdapterZippedHelp(adapterId);
    }

    public ExtractorDescriptor getExtractor(String id) {
        return extractorsClient.getExtractorDescriptor(id);
    }

    public Set<String> getExtractorsId() {
        return extractorsClient.getExtractors();
    }

    public String getExtractorHelpSha1(String extractorId) {
        return extractorsClient.getExtractorHelpSha1(extractorId);
    }

    public InputStream getExtractorZippedHelp(String extractorId) throws IOException {
        return extractorsClient.getExtractorZippedHelp(extractorId);
    }

    public MeasureDescriptor getMeasure(String id) {
        return measuresClient.getMeasureDescriptor(id);
    }

    public Set<String> getMeasuresId() {
        return measuresClient.getMeasures();
    }

    public String getMeasureHelpSha1(String measureId) {
        return measuresClient.getMeasureHelpSha1(measureId);
    }

    public InputStream getMeasureZippedHelp(String measureId) throws IOException {
        return measuresClient.getMeasureZippedHelp(measureId);
    }

    public Comparison getComparison(Comparison comparison) {
        comparison = comparisonClient.getComparison(comparison.getId());
        comparison.setSlave(url);
        return comparison;
    }

    public Comparison submit(Comparison comparison, InputStream dataset1Stream, InputStream dataset2Stream) throws IOException {
        String comparisonId = comparisonClient.submit(comparison, dataset1Stream, dataset2Stream);
        comparison.setId(comparisonId);
        comparison.setSlave(url);
        return comparison;
    }

    public List<String> submit(String adapter, String extractor, String measure,
            List<String> datasetsNames, List<File> datasetsFiles,
            List<Integer> referenceDatasets) throws IOException {
        List<String> ids = comparisonClient.submitFiles(
                adapter, extractor, measure,
                datasetsNames, datasetsFiles, referenceDatasets);
        return ids;
    }

    public boolean supportComparison(Comparison comparison) {
        return supportComparison(comparison.getAdapterId(),
                comparison.getExtractorId(), comparison.getMeasureId());
    }

    public boolean supportComparison(String adapterId, String extractorId, String measureId) {
        return comparisonClient.supportComparison(adapterId, extractorId, measureId);
    }

    public long getNodeStatus() {
        RetryService<String> rs = new RetryService<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return nodeStatusClient.get(String.class);
                    }
                }, getTimeout, getRetry);
        return Long.parseLong(rs.run());
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Slave)) {
            return false;
        }
        final Slave other = (Slave) obj;
        return this.url.equals(other.url);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }
}
