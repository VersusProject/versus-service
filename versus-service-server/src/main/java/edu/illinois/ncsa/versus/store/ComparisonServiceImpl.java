package edu.illinois.ncsa.versus.store;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import com.google.inject.Inject;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;

/**
 * Implementation of repository service for comparisons.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class ComparisonServiceImpl implements ComparisonService {

    private final ComparisonProcessor transactionLog;

    private final FileProcessor fileProcessor;

    @Inject
    public ComparisonServiceImpl(ComparisonProcessor transactionLog,
            FileProcessor fileProcessor) {
        this.transactionLog = transactionLog;
        this.fileProcessor = fileProcessor;
    }

    @Override
    public void addComparison(Comparison comparison) {
        transactionLog.addComparison(comparison);
    }

    @Override
    public Comparison getComparison(String id) {
        return transactionLog.getComparison(id);
    }

    @Override
    public Collection<Comparison> listAll() {
        return transactionLog.listAll();
    }

    @Override
    public void updateValue(String id, String value) {
        transactionLog.updateValue(id, value);
    }

    @Override
    public String addFile(InputStream inputStream) throws Exception {
        return fileProcessor.addFile(inputStream, null);
    }

    @Override
    public String addFile(InputStream inputStream, String filename) 
            throws Exception{
        return fileProcessor.addFile(inputStream, filename);
    }

    @Override
    public InputStream getFile(String id) throws Exception {
        return fileProcessor.getFile(id);
    }

    public void setStatus(String id, ComparisonStatus status) {
        transactionLog.setStatus(id, status);
    }
    
    @Override
    public void setError(String id, String error) {
        transactionLog.setError(id, error);
    }

    @Override
    public String findComparison(String file1, String file2, String adapter,
            String extractor, String measure) {

        String cid = null;
        Collection<Comparison> comparisons = transactionLog.listAll();

        if (comparisons.isEmpty()) {
            return cid;
        }
        Iterator<Comparison> itr = comparisons.iterator();
        while (itr.hasNext()) {
            Comparison c = itr.next();
            if (adapter.equals(c.getAdapterId())
                    && extractor.equals(c.getExtractorId())
                    && measure.equals(c.getMeasureId())
                    && file1.equals(c.getFirstDataset())
                    && file2.equals(c.getSecondDataset())) {
                cid = c.getId();
            }
        }

        return cid;
    }
}
