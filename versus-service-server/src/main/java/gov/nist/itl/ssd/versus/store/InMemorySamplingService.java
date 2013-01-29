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
package gov.nist.itl.ssd.versus.store;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import gov.nist.itl.ssd.sampling.Sampling;
import gov.nist.itl.ssd.sampling.Sampling.SamplingStatus;

/**
 *
 * @author antoinev
 */
public class InMemorySamplingService implements SamplingService {

    private static final ConcurrentHashMap<String, Sampling> samplings =
            new ConcurrentHashMap<String, Sampling>();
    
    @Override
    public void addSampling(Sampling sampling) {
        samplings.putIfAbsent(sampling.getId(), sampling);
    }
    
    @Override
    public Sampling getSampling(String id) {
        return samplings.get(id);
    }
    
    @Override
    public Collection<Sampling> listAll() {
        return samplings.values();
    }
    
    @Override
    public void updateSample(String id, List<String> sample) {
        Sampling sampling = samplings.get(id);
        if(sampling != null) {
            sampling.setSample(sample);
        } else {
            throw new RuntimeException("Sampling " + id + " not found.");
        }
    }
    
    @Override
    public void setStatus(String id, SamplingStatus status) {
        Sampling sampling = samplings.get(id);
        if(sampling != null) {
            sampling.setStatus(status);
        } else {
            throw new RuntimeException("Sampling " + id + " not found.");
        }
    }

    @Override
    public void setError(String id, String error) {
        Sampling sampling = samplings.get(id);
        if(sampling != null) {
            sampling.setError(error);
        } else {
            throw new RuntimeException("Sampling " + id + " not found.");
        }
    }
}
