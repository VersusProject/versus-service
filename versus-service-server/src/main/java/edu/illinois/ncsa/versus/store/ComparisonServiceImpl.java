package edu.illinois.ncsa.versus.store;

import java.io.InputStream;
import java.util.Collection;

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
}
