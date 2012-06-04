package edu.illinois.ncsa.versus.core.comparison;

import java.io.Serializable;
import java.util.UUID;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * DTO for a single pairwise comparison.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
@XStreamAlias("comparison")
public class Comparison implements Serializable {

    public enum ComparisonStatus {

        STARTED, DONE, FAILED, ABORTED

    }
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

    @XStreamAlias("slave")
    private String slave;

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

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
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
