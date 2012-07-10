package edu.illinois.ncsa.versus.store;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import com.google.inject.Inject;

import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.engine.impl.PairwiseComparison;
import edu.illinois.ncsa.versus.restlet.Comparison;

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
	public void updateValue(String id, double value) {
		transactionLog.updateValue(id, String.valueOf(value));
	}

	@Override
	public String addFile(InputStream inputStream) {
		return fileProcessor.addFile(inputStream, null);
	}

	@Override
	public String addFile(InputStream inputStream, String filename) {
		return fileProcessor.addFile(inputStream, filename);
	}

	@Override
	public InputStream getFile(String id) {
		return fileProcessor.getFile(id);
	}

	public void setStatus(String id, ComparisonStatus status) {
		transactionLog.setStatus(id, status);
	}

	@Override
	public String findComparison(String file1, String file2, String adapter,
			String extractor, String measure) {

		String cid = null;
		Collection<Comparison> comparisons = transactionLog.listAll();

		if (comparisons.size() == 0) {
			return cid;
		}
		Iterator<Comparison> itr = comparisons.iterator();
		while (itr.hasNext()) {
			Comparison c = itr.next();
			if (adapter == c.getAdapterId() && extractor == c.getExtractorId()
					&& measure == c.getMeasureId()
					&& file1 == c.getFirstDataset()
					&& file2 == c.getSecondDataset()) {
				cid = c.getId();
			}
		}

		return cid;
	}

	public void addComparison(PairwiseComparison pairwiseComparison,
			String datasetUri1, String datasetUri2) {
		// TODO try to be consistent and pick either Comparison or
		// PairwiseComparison
		Comparison comparison = new Comparison();
		comparison.setId(pairwiseComparison.getId());

		comparison.setFirstDataset(datasetUri1);
		comparison.setSecondDataset(datasetUri2);

		comparison.setAdapterId(pairwiseComparison.getAdapterId());
		comparison.setExtractorId(pairwiseComparison.getExtractorId());
		comparison.setMeasureId(pairwiseComparison.getMeasureId());
		addComparison(comparison);
	}
}
