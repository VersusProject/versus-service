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

import gov.nist.itl.ssd.sampling.Sampling;
import gov.nist.itl.ssd.sampling.Sampling.SamplingStatus;

/**
 *
 * @author antoinev
 */
public interface SamplingService {

    void addSampling(Sampling sampling);

    Sampling getSampling(String id);

    Collection<Sampling> listAll();

    void updateSample(String id, List<String> sample);

    void setStatus(String id, SamplingStatus status);

    void setError(String id, String error);
}
