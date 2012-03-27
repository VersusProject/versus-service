package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.illinois.ncsa.versus.restlet.Comparison;
import edu.illinois.ncsa.versus.restlet.Distribution;


public class DistributionServiceImpl implements DistributionService {

	private final static ConcurrentMap<String, Distribution> distributions = new ConcurrentHashMap<String, Distribution>();
	
	@Override
	public void addDistribution(Distribution distribution) {
		distributions.put(distribution.getId(), distribution);
	}

	@Override
	public Distribution getDistribution(String id) {
		return distributions.get(id);
	}

	@Override
	public Distribution getDistribution(String file1, String file2,
			String adapter, String extractor, String measure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Distribution> listAll() {
		return distributions.values();
	}

	@Override
	public void updateMean(String id, double mean) {
		Distribution distribution = distributions.get(id);
		if (distribution != null) {
			distribution.setMean(mean);
		}
		distributions.put(distribution.getId(), distribution);
	}		
	
	@Override
	public void updateStdDev(String id, double sigma) {
		Distribution distribution = distributions.get(id);
		if (distribution != null) {
			distribution.setDeviation(sigma);
		}
		distributions.put(distribution.getId(), distribution);
	}			

}