package edu.illinois.ncsa.versus.store;

import java.util.Collection;

import edu.illinois.ncsa.versus.engine.impl.Job.ComparisonStatus;
import edu.illinois.ncsa.versus.restlet.Comparison;

/**
 * Manipulate storage of comparisons in repository.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public interface ComparisonProcessor {

	/**
	 * Add new comparison.
	 * 
	 * @param comparison
	 *            the comparison
	 */
	void addComparison(Comparison comparison);

	/**
	 * Get a comparison by id.
	 * 
	 * @param id
	 *            id of comparison
	 * @return comparison
	 */
	Comparison getComparison(String id);

	/**
	 * Retrieve all known comparisons.
	 * 
	 * @return a collection of all known comparisons.
	 */
	Collection<Comparison> listAll();

	/**
	 * Update the similarity score of a specific comparison.
	 * 
	 * @param id
	 *            the id of the comparison to update
	 * @param value
	 *            the new similarity score
	 */
	void updateValue(String id, String value);

	void setStatus(String id, ComparisonStatus status);

	ComparisonStatus getStatus(String id);

}
