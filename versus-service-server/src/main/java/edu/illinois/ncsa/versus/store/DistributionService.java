package edu.illinois.ncsa.versus.store;

import java.util.Collection;

import edu.illinois.ncsa.versus.restlet.Distribution;

/**
 * Handles the manipulation of distributions in the repository.
 * 
 * @author Devin Bonnie
 * 
 */
public interface DistributionService {

	void addDistribution(Distribution distribution);

	Distribution getDistribution(String id);
		
	Collection<Distribution> listAll();
}
