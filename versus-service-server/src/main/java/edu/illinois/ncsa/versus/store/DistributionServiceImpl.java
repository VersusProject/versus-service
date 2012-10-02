package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
	public Collection<Distribution> listAll() {
		return distributions.values();
	}
}