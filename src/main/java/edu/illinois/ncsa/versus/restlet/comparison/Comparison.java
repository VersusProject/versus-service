package edu.illinois.ncsa.versus.restlet.comparison;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;

/**
 * DTO for a single pairwise comparison.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
@XStreamAlias("comparison")
public class Comparison implements Serializable {

    @XStreamAlias("id")
    private String id;

    @XStreamAlias("firstDataset")
    private String firstDataset;

    @XStreamAlias("secondDataset")
    private String secondDataset;

    @XStreamAlias("adapter")
    private String adapterId;

    @XStreamAlias("extractor")
    private String extractorId;

    @XStreamAlias("measure")
    private String measureId;

    @XStreamAlias("value")
    private String value;

    @XStreamAlias("status")
    private ComparisonStatus status;

    public Comparison() {
        id = UUID.randomUUID().toString();
    }

    public Comparison(String firstDataset, String secondDataset,
            String adapterId, String extractorId, String measureId) {
        this();
        this.firstDataset = firstDataset;
        this.secondDataset = secondDataset;
        this.adapterId = adapterId;
        this.extractorId = extractorId;
        this.measureId = measureId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFirstDataset() {
        return firstDataset;
    }

    public void setFirstDataset(String firstDataset) {
        this.firstDataset = firstDataset;
    }

    public String getSecondDataset() {
        return secondDataset;
    }

    public void setSecondDataset(String secondDataset) {
        this.secondDataset = secondDataset;
    }

    public String getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(String adapterId) {
        this.adapterId = adapterId;
    }

    public String getExtractorId() {
        return extractorId;
    }

    public void setExtractorId(String extractorId) {
        this.extractorId = extractorId;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public ComparisonStatus getStatus() {
        return status;
    }

    public void setStatus(ComparisonStatus status) {
        this.status = status;

    }

    PairwiseComparison toPairwiseComparison() throws IOException {
        PairwiseComparison pairwiseComparison = new PairwiseComparison();
        pairwiseComparison.setId(id);
        pairwiseComparison.setFirstDataset(getFile(firstDataset));
        pairwiseComparison.setSecondDataset(getFile(secondDataset));
        pairwiseComparison.setAdapterId(adapterId);
        pairwiseComparison.setExtractorId(extractorId);
        pairwiseComparison.setMeasureId(measureId);
        return pairwiseComparison;
    }

    /**
     * Make temp copy of remote file.
     *
     * @param remoteURL
     * @return
     * @throws IOException
     */
    private File getFile(String remoteURL) throws IOException {
        URL url = new URL(remoteURL);
        File file;
        if (url.getPath().isEmpty() || url.getPath().matches(".*/versus[\\d]+.tmp")) {
            file = File.createTempFile("versus", ".tmp");
        } else {
            String filename = new File(url.getPath()).getName().replaceAll("[\\d]+\\.", ".");
            int idx = filename.lastIndexOf('.');
            if (idx > 3) {
                file = File.createTempFile(filename.substring(0, idx), filename.substring(idx));
            } else if (idx != -1) {
                file = File.createTempFile("versus", filename.substring(idx));
            } else {
                file = File.createTempFile(filename, ".tmp");
            }
        }
        file.deleteOnExit(); // TODO only gets called when jvm exits
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(file);
            is = url.openStream();
            byte[] buff = new byte[10240];
            int len;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Comparison)) {
            return false;
        }
        Comparison other = (Comparison) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
