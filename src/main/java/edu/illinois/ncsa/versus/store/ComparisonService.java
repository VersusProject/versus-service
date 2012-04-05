package edu.illinois.ncsa.versus.store;

import java.io.InputStream;
import java.util.Collection;

import edu.illinois.ncsa.versus.restlet.comparison.Comparison;

/**
 * Handles the manipulation of comparisons in the repository.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public interface ComparisonService {

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
	void updateValue(String id, double value);

	/**
	 * Store file stream.
	 * 
	 * @param inputStream
	 * @return unique identifier for file
	 */
	String addFile(InputStream inputStream);

	/**
	 * Store file stream.
	 * 
	 * @param inputStream
	 * @param filename the original filename
	 * @return unique identifier for file
	 */
	String addFile(InputStream inputStream, String filename);

	InputStream getFile(String id);

}
