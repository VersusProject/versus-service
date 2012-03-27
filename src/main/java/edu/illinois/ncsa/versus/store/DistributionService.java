package edu.illinois.ncsa.versus.store;

import java.io.InputStream;
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
	
	Distribution getDistribution(String file1, String file2, String adapter, String extractor, String measure);
	
	Collection<Distribution> listAll();

	void updateMean(String id, double mean);
	
	void updateStdDev(String id, double sigma);

}
