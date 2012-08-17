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
package gov.nist.itl.ssd.sampling.restlet;

import gov.nist.itl.ssd.sampling.Sampling;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import gov.nist.itl.ssd.sampling.Individual;
import gov.nist.itl.ssd.sampling.Sampling.SamplingStatus;
import gov.nist.itl.ssd.sampling.SamplingExecutionEngine;
import gov.nist.itl.ssd.sampling.SamplingRegistry;
import gov.nist.itl.ssd.sampling.SamplingRequest;
import gov.nist.itl.ssd.sampling.SamplingStatusHandler;
import gov.nist.itl.ssd.versus.store.SamplingService;

/**
 *
 * @author antoinev
 */
public class SamplingSubmitter {

    private final Sampling sampling;

    private final List<InputStream> datasetsStreams;

    private final ServerApplication server;

    public SamplingSubmitter(ServerApplication server, Sampling sampling,
            List<InputStream> datasetsStreams) {
        this.server = server;
        this.sampling = sampling;
        this.datasetsStreams = datasetsStreams;
    }

    public Sampling submit() throws NoSlaveAvailableException {
        final SamplingService samplingService = ServerApplication.getInjector().
                getInstance(SamplingService.class);
        Sampling result = sampling;
        SamplingRegistry registry = server.getSamplingRegistry();
        if (registry.supportSampling(sampling.getIndividual(),
                sampling.getSampler())) {
            samplingService.addSampling(sampling);
            SamplingRequest sr = new SamplingRequest(sampling.getId(),
                    sampling.getIndividual(), sampling.getSampler(),
                    sampling.getSampleSize(), sampling.getDatasets(),
                    datasetsStreams);
            submit(sr);
        } else {
            throw new NoSlaveAvailableException();
        }
        return result;
    }

    private void submit(final SamplingRequest sampling) {
        SamplingExecutionEngine engine = server.getSamplingEngine();

        final SamplingService samplingService = ServerApplication.getInjector().
                getInstance(SamplingService.class);

        engine.submit(sampling, new SamplingStatusHandler() {

            @Override
            public void onStarted() {
                samplingService.setStatus(sampling.getId(), SamplingStatus.STARTED);
            }

            @Override
            public void onDone() {
                List<Individual> result = sampling.getResult();
                List<String> sample = new ArrayList<String>(result.size());
                for (Individual ind : result) {
                    sample.add(ind.getId());
                }
                samplingService.updateSample(sampling.getId(), sample);
                samplingService.setStatus(sampling.getId(), SamplingStatus.DONE);
            }

            @Override
            public void onFailed() {
                samplingService.setStatus(sampling.getId(), SamplingStatus.FAILED);
            }

            @Override
            public void onAborted() {
                samplingService.setStatus(sampling.getId(), SamplingStatus.ABORTED);
            }
        });

    }
}
