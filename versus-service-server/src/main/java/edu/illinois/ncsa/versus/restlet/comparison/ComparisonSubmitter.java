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
package edu.illinois.ncsa.versus.restlet.comparison;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.ComparisonStatusHandler;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.RankSlaves;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.Slave;
import edu.illinois.ncsa.versus.restlet.SlavesManager;
import edu.illinois.ncsa.versus.restlet.SlavesManager.SlaveValue;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;

/**
 *
 * @author antoinev
 */
public class ComparisonSubmitter {

    private final Comparison comparison;

    private final InputStream dataset1Stream;

    private final InputStream dataset2Stream;

    private final CompareRegistry registry;

    private final ExecutionEngine engine;

    private final SlavesManager slavesManager;

    private static final Logger logger = Logger.getLogger(ComparisonSubmitter.class.getName());

    public ComparisonSubmitter(ServerApplication server, Comparison comparison)
            throws MalformedURLException, IOException {
        URL url1 = new URL(comparison.getFirstDataset());
        URL url2 = new URL(comparison.getSecondDataset());
        this.registry = server.getRegistry();
        this.engine = server.getEngine();
        this.slavesManager = server.getSlavesManager();
        this.comparison = comparison;
        this.dataset1Stream = url1.openStream();
        this.dataset2Stream = url2.openStream();
    }

    public ComparisonSubmitter(ServerApplication server, Comparison comparison,
            InputStream dataset1Stream, InputStream dataset2Stream) {
        this.registry = server.getRegistry();
        this.engine = server.getEngine();
        this.slavesManager = server.getSlavesManager();
        this.comparison = comparison;
        this.dataset1Stream = dataset1Stream;
        this.dataset2Stream = dataset2Stream;
    }

    public ComparisonSubmitter(CompareRegistry registry, ExecutionEngine engine,
            SlavesManager slavesManager, Comparison comparison,
            InputStream dataset1Stream, InputStream dataset2Stream) {
        this.registry = registry;
        this.engine = engine;
        this.slavesManager = slavesManager;
        this.comparison = comparison;
        this.dataset1Stream = dataset1Stream;
        this.dataset2Stream = dataset2Stream;
    }

    public Comparison submit()
            throws IOException, NoSlaveAvailableException {

        final ComparisonServiceImpl comparisonService =
                ServerApplication.getInjector().getInstance(ComparisonServiceImpl.class);
        Comparison result = comparison;

        boolean supportLocal = registry.supportComparison(comparison.getAdapterId(),
                comparison.getExtractorId(), comparison.getMeasureId());
        long waitingJobsNumber = engine.getWaitingJobsNumber();

        if (supportLocal && waitingJobsNumber == 0) {
            submitLocal(comparisonService, result);
            return result;
        }

        SlaveValue bestSlave = querySlaves();

        if (bestSlave == null) {
            if (!supportLocal) {
                throw new NoSlaveAvailableException();
            }
            submitLocal(comparisonService, result);
            return result;
        }

        if (supportLocal && waitingJobsNumber <= bestSlave.value) {
            submitLocal(comparisonService, result);
            return result;
        }

        Slave slave = bestSlave.slave;
        logger.log(Level.INFO, "Forwarding comparison request to {0}",
                slave);
        result = slave.submit(comparison, dataset1Stream, dataset2Stream);
        comparisonService.addComparison(result);
        return result;
    }

    private void submitLocal(ComparisonServiceImpl comparisonService, Comparison result) {
        comparisonService.addComparison(result);
        PairwiseComparison pairwiseComparison = new PairwiseComparison();
        pairwiseComparison.setId(comparison.getId());
        pairwiseComparison.setAdapterId(comparison.getAdapterId());
        pairwiseComparison.setExtractorId(comparison.getExtractorId());
        pairwiseComparison.setMeasureId(comparison.getMeasureId());
        pairwiseComparison.setFirstDataset(dataset1Stream);
        pairwiseComparison.setSecondDataset(dataset2Stream);
        submit(pairwiseComparison);
    }

    /**
     * Find which slaves support requested methods. Forward to the first slave
     * in the list.
     *
     * @param entity
     * @param comparison
     * @return
     */
    private SlaveValue querySlaves() throws IOException, NoSlaveAvailableException {
        Set<Slave> supportingSlavesSet = slavesManager.getSlaves(
                new SlavesManager.SlaveQuery<Boolean>() {
                    @Override
                    public Boolean executeQuery(Slave slave) {
                        return slave.supportComparison(comparison);
                    }
                });
        SlaveValue minSlave = slavesManager.getMinSlave(supportingSlavesSet,
                new SlavesManager.SlaveQuery<Long>() {
                    @Override
                    public Long executeQuery(Slave slave) {
                        return slave.getNodeStatus();
                    }
                });

        return minSlave;
    }

    /**
     * Submit to execution engine.
     *
     * @param comparison
     */
    private void submit(final PairwiseComparison comparison) {

        final ComparisonServiceImpl comparisonService =
                ServerApplication.getInjector().getInstance(ComparisonServiceImpl.class);

        engine.submit(comparison, new ComparisonStatusHandler() {
            @Override
            public void onDone(double value) {
                logger.log(
                        Level.INFO, "Comparison {0} done. Result is: {1}",
                        new Object[]{comparison.getId(), value});
                comparisonService.setStatus(comparison.getId(),
                        ComparisonStatus.DONE);
                comparisonService.updateValue(comparison.getId(), String.valueOf(value));
            }

            @Override
            public void onStarted() {
                logger.log(Level.INFO, "Comparison {0} started.",
                        comparison.getId());
                comparisonService.setStatus(comparison.getId(),
                        ComparisonStatus.STARTED);
            }

            @Override
            public void onFailed(String msg, Throwable e) {
                logger.log(Level.INFO,
                        "Comparison " + comparison.getId() + " failed. " + msg, e);
                comparisonService.setStatus(comparison.getId(),
                        ComparisonStatus.FAILED);
                comparisonService.setError(comparison.getId(), msg);
            }

            @Override
            public void onAborted(String msg) {
                logger.log(Level.INFO, "Comparison {0} aborted. {1}",
                        new Object[]{comparison.getId(), msg});
                comparisonService.setStatus(comparison.getId(),
                        ComparisonStatus.ABORTED);
            }
        });
    }
}
