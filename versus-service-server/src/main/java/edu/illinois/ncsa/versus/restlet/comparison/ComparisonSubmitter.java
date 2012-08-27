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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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

    private final CompareRegistry registry;

    private final ExecutionEngine engine;

    private final SlavesManager slavesManager;

    private final String adapter;

    private final String extractor;

    private final String measure;

    private final List<String> datasetsNames;

    private final List<InputStream> datasetsStreams;

    private final List<Integer> referenceDatasets;

    private static final Logger logger = Logger.getLogger(ComparisonSubmitter.class.getName());

    private static final ComparisonServiceImpl comparisonService =
            ServerApplication.getInjector().getInstance(ComparisonServiceImpl.class);

    public ComparisonSubmitter(ServerApplication server,
            String adapter, String extractor, String measure,
            List<String> datasetsNames, List<InputStream> datasetsStreams,
            List<Integer> referenceDatasets)
            throws MalformedURLException, IOException {
        this.registry = server.getRegistry();
        this.engine = server.getEngine();
        this.slavesManager = server.getSlavesManager();

        this.adapter = adapter;
        this.extractor = extractor;
        this.measure = measure;
        this.datasetsNames = datasetsNames;
        this.datasetsStreams = datasetsStreams;
        this.referenceDatasets = referenceDatasets;
    }

    public ComparisonSubmitter(ServerApplication server, Comparison comparison,
            InputStream dataset1Stream, InputStream dataset2Stream) {
        this.registry = server.getRegistry();
        this.engine = server.getEngine();
        this.slavesManager = server.getSlavesManager();

        this.adapter = comparison.getAdapterId();
        this.extractor = comparison.getExtractorId();
        this.measure = comparison.getMeasureId();
        this.datasetsNames = new ArrayList<String>(2);
        datasetsNames.add(comparison.getFirstDataset());
        datasetsNames.add(comparison.getSecondDataset());
        this.datasetsStreams = new ArrayList<InputStream>(2);
        datasetsStreams.add(dataset1Stream);
        datasetsStreams.add(dataset2Stream);
        this.referenceDatasets = null;
    }

    public ComparisonSubmitter(CompareRegistry registry, ExecutionEngine engine,
            SlavesManager slavesManager, Comparison comparison,
            InputStream dataset1Stream, InputStream dataset2Stream) {
        this.registry = registry;
        this.engine = engine;
        this.slavesManager = slavesManager;

        this.adapter = comparison.getAdapterId();
        this.extractor = comparison.getExtractorId();
        this.measure = comparison.getMeasureId();
        this.datasetsNames = new ArrayList<String>(2);
        datasetsNames.add(comparison.getFirstDataset());
        datasetsNames.add(comparison.getSecondDataset());
        this.datasetsStreams = new ArrayList<InputStream>(2);
        datasetsStreams.add(dataset1Stream);
        datasetsStreams.add(dataset2Stream);
        this.referenceDatasets = null;
    }

    public ComparisonSubmitter(ServerApplication server, Comparison comparison)
            throws MalformedURLException, IOException {
        URL url1 = new URL(comparison.getFirstDataset());
        URL url2 = new URL(comparison.getSecondDataset());
        InputStream dataset1Stream = url1.openStream();
        InputStream dataset2Stream = url2.openStream();

        this.registry = server.getRegistry();
        this.engine = server.getEngine();
        this.slavesManager = server.getSlavesManager();

        this.adapter = comparison.getAdapterId();
        this.extractor = comparison.getExtractorId();
        this.measure = comparison.getMeasureId();
        this.datasetsNames = new ArrayList<String>(2);
        datasetsNames.add(comparison.getFirstDataset());
        datasetsNames.add(comparison.getSecondDataset());
        this.datasetsStreams = new ArrayList<InputStream>(2);
        datasetsStreams.add(dataset1Stream);
        datasetsStreams.add(dataset2Stream);
        this.referenceDatasets = null;
    }

    public List<String> submit()
            throws IOException, NoSlaveAvailableException {

        boolean supportLocal = registry.supportComparison(adapter,
                extractor, measure);
        long waitingJobsNumber = engine.getWaitingJobsNumber();

        HashMap<Slave, List<Integer>> slavesAssociation =
                associateComparisonsWithSlave(supportLocal, waitingJobsNumber);

        ArrayList<String> comparisons = new ArrayList<String>(datasetsNames.size());

        for (Slave slave : slavesAssociation.keySet()) {
            if (slave == null) {
                comparisons.addAll(submitLocal(slavesAssociation.get(null)));
            } else {
                comparisons.addAll(submitToSlave(slave, slavesAssociation.get(slave)));
            }
        }

        return comparisons;
    }

    private HashMap<Slave, List<Integer>> associateComparisonsWithSlave(
            boolean supportLocal, long localWaiting)
            throws NoSlaveAvailableException {
        Set<Slave> supportingSlavesSet = slavesManager.getSlaves(
                new SlavesManager.SlaveQuery<Boolean>() {
                    @Override
                    public Boolean executeQuery(Slave slave) {
                        return slave.supportComparison(adapter, extractor, measure);
                    }
                });

        if (supportingSlavesSet.isEmpty() && !supportLocal) {
            throw new NoSlaveAvailableException();
        }

        TreeSet<SlaveValue> sortedSlaves = slavesManager.getSortedSlaves(supportingSlavesSet,
                new SlavesManager.SlaveQuery<Long>() {
                    @Override
                    public Long executeQuery(Slave slave) {
                        return slave.getNodeStatus();
                    }
                });

        if (supportLocal) {
            sortedSlaves.add(new SlaveValue(null, localWaiting));
        }
        HashMap<Slave, List<Integer>> result = new HashMap<Slave, List<Integer>>();

        if (referenceDatasets == null || referenceDatasets.isEmpty()) {
            for (int i = 0; i < datasetsNames.size() - 1; i++) {
                associate(i, sortedSlaves, result);
            }
        } else {
            for (int i : referenceDatasets) {
                associate(i, sortedSlaves, result);
            }
        }

        return result;
    }

    private void associate(int i, TreeSet<SlaveValue> sortedSlaves,
            HashMap<Slave, List<Integer>> result) {
        SlaveValue first = sortedSlaves.first();
        List<Integer> list = result.get(first.slave);
        if (list == null) {
            list = new ArrayList<Integer>();
            result.put(first.slave, list);
        }
        list.add(i);
        SlaveValue sv = new SlaveValue(first.slave,
                first.value + datasetsNames.size() - 1 - i);
        sortedSlaves.remove(first);
        sortedSlaves.add(sv);
        result.put(first.slave, list);

    }

    private List<String> submitToSlave(Slave slave, List<Integer> referenceImagesIdx)
            throws IOException {
        
        int firstImage = referenceImagesIdx.get(0);
        List<String> slaveDatasetsNames = 
                datasetsNames.subList(firstImage, datasetsNames.size());
        List<InputStream> slaveInputStreams = 
                datasetsStreams.subList(firstImage, datasetsStreams.size());
        
        List<Integer> referencesDatasets = new ArrayList<Integer>(referenceImagesIdx.size());
        for(int i : referenceImagesIdx) {
            referencesDatasets.add(i - firstImage);
        }
        
        return slave.submit(adapter, extractor, measure,
                slaveDatasetsNames, slaveInputStreams, referencesDatasets);
    }

    private List<String> submitLocal(List<Integer> referenceImagesIdx) {
        ArrayList<String> result = new ArrayList<String>();
        for (Integer i : referenceImagesIdx) {
            for (int j = i + 1; j < datasetsNames.size(); j++) {
                Comparison comparison = new Comparison(
                        datasetsNames.get(i), datasetsNames.get(j),
                        adapter, extractor, measure);
                submitLocal(comparison,
                        datasetsStreams.get(i), datasetsStreams.get(j));
                result.add(comparison.getId());
            }
        }
        return result;
    }

    private void submitLocal(Comparison comparison,
            InputStream stream1, InputStream stream2) {
        comparisonService.addComparison(comparison);
        PairwiseComparison pairwiseComparison = new PairwiseComparison();
        pairwiseComparison.setId(comparison.getId());
        pairwiseComparison.setAdapterId(comparison.getAdapterId());
        pairwiseComparison.setExtractorId(comparison.getExtractorId());
        pairwiseComparison.setMeasureId(comparison.getMeasureId());
        pairwiseComparison.setFirstDataset(stream1);
        pairwiseComparison.setSecondDataset(stream2);
        submit(pairwiseComparison);
    }

    /**
     * Submit to execution engine.
     *
     * @param comparison
     */
    private void submit(final PairwiseComparison comparison) {
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
