package edu.illinois.ncsa.versus.store;

import java.util.Collection;

import com.google.inject.Inject;

import edu.illinois.ncsa.versus.restlet.Comparison;

/**
 * Implementation of repository service for comparisons.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class ComparisonServiceImpl implements ComparisonService {

	private final ComparisonProcessor transactionLog;

	@Inject
	public ComparisonServiceImpl(ComparisonProcessor transactionLog) {
		this.transactionLog = transactionLog;
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
}
