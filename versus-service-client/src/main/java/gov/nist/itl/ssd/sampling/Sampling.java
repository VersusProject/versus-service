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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author antoinev
 */
@XStreamAlias("sampling")
public class Sampling implements Serializable {
    
    public enum SamplingStatus {
        STARTED, DONE, FAILED, ABORTED
    }
    
    @XStreamAlias("id")
    private final String id;
    
    @XStreamAlias("individual")
    private final String individual;
    
    @XStreamAlias("sampler")
    private final String sampler;
    
    @XStreamAlias("sampleSize")
    private final int sampleSize;
    
    @XStreamAlias("datasets")
    @XStreamConverter(ListDatasetsConverter.class)
    private final List<String> datasets;
    
    @XStreamAlias("sample")
    @XStreamConverter(SampleConverter.class)
    private final List<String> sample;
    
    @XStreamAlias("status")
    private SamplingStatus status;
    
    public Sampling(String individual, String sampler, int sampleSize,
            List<String> datasets) {
        this.id = UUID.randomUUID().toString();
        this.individual = individual;
        this.sampler = sampler;
        this.sampleSize = sampleSize;
        this.datasets = new ArrayList<String>(datasets);
        this.sample = new ArrayList<String>(sampleSize);
    }
    
    public String getId() {
        return id;
    }
    
    public String getIndividual() {
        return individual;
    }
    
    public String getSampler() {
        return sampler;
    }
    
    public int getSampleSize() {
        return sampleSize;
    }
    
    public List<String> getDatasets() {
        return Collections.unmodifiableList(datasets);
    }
    
    public List<String> getSample() {
        return Collections.unmodifiableList(sample);
    }
    
    public void setSample(Collection<String> sample) {
        this.sample.clear();
        this.sample.addAll(sample);
    }
    
    public SamplingStatus getStatus() {
        return status;
    }
    
    public void setStatus(SamplingStatus status) {
        this.status = status;
    }
}
