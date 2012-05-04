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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.illinois.ncsa.versus.engine.impl.ComparisonStatusHandler;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.engine.impl.Job;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.RankSlaves;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.Slave;
import edu.illinois.ncsa.versus.restlet.SlavesManager;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import edu.illinois.ncsa.versus.store.RepositoryModule;

/**
 *
 * @author antoinev
 */
public class ComparisonSubmitter {

    private final Comparison comparison;
    private final ServerApplication server;
    
    private static final Logger logger = Logger.getLogger(ComparisonSubmitter.class.getName());

    public ComparisonSubmitter(ServerApplication server, Comparison comparison) {
        this.server = server;
        this.comparison = comparison;
    }

    public Comparison submit()
            throws IOException, NoSlaveAvailableException {
        Comparison result = comparison;
        
        CompareRegistry registry = server.getRegistry();
        if (registry.supportComparison(comparison.getAdapterId(),
                comparison.getExtractorId(), comparison.getMeasureId())) {
            PairwiseComparison pairwiseComparison;
            pairwiseComparison = comparison.toPairwiseComparison();
            submit(pairwiseComparison);
        } else {
            result = querySlaves(comparison);
            if (comparison == null) {
                throw new NoSlaveAvailableException();
            }
        }

        // Guice storage
        Injector injector = Guice.createInjector(new RepositoryModule());
        ComparisonServiceImpl comparisonService =
                injector.getInstance(ComparisonServiceImpl.class);
        comparisonService.addComparison(result);

        return result;
    }

    /**
     * Find which slaves support requested methods. Forward to the first slave
     * in the list.
     *
     * @param entity
     * @param comparison
     * @return
     */
    private Comparison querySlaves(final Comparison comparison) {
        // TODO: each slave should give a score telling how it is willing to
        // run the comparison
        Set<Slave> supportingSlavesSet = server.getSlavesManager().
                getSlaves(new SlavesManager.SlaveQuery<Boolean>() {

            @Override
            public Boolean executeQuery(Slave slave) {
                return slave.supportComparison(comparison);
            }
        });

        List<Slave> supportingSlaves = new ArrayList<Slave>(supportingSlavesSet);
        // rank slaves
        supportingSlaves = RankSlaves.rank(supportingSlaves);
        // forward to first slave
        if (supportingSlaves.isEmpty()) {
            return null;
        }
        Slave slave = supportingSlaves.get(0);
        logger.log(Level.INFO, "Forwarding comparison request to {0}",
                slave);
        return slave.submit(comparison);
    }

    /**
     * Submit to execution engine.
     *
     * @param comparison
     */
    private void submit(final PairwiseComparison comparison) {

        ExecutionEngine engine = server.getEngine();
        final ComparisonServiceImpl comparisonService =
                ServerApplication.getInjector().getInstance(ComparisonServiceImpl.class);

        engine.submit(comparison, new ComparisonStatusHandler() {

            @Override
            public void onDone(double value) {
                logger.log(
                        Level.INFO, "Comparison {0} done. Result is: {1}",
                        new Object[]{comparison.getId(), value});
                comparisonService.setStatus(comparison.getId(),
                        Job.ComparisonStatus.DONE);
                comparisonService.updateValue(comparison.getId(), String.valueOf(value));
            }

            @Override
            public void onStarted() {
                logger.log(Level.INFO, "Comparison {0} started.",
                        comparison.getId());
                comparisonService.setStatus(comparison.getId(),
                        Job.ComparisonStatus.STARTED);
            }

            @Override
            public void onFailed(String msg, Throwable e) {
                logger.log(Level.INFO,
                        "Comparison " + comparison.getId() + " failed. " + msg, e);
                comparisonService.setStatus(comparison.getId(),
                        Job.ComparisonStatus.FAILED);

            }

            @Override
            public void onAborted(String msg) {
                logger.log(Level.INFO, "Comparison {0} aborted. {1}",
                        new Object[]{comparison.getId(), msg});
                comparisonService.setStatus(comparison.getId(),
                        Job.ComparisonStatus.ABORTED);
            }
        });
    }
}
