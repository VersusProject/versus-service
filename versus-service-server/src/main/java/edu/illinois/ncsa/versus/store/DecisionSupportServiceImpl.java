package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.illinois.ncsa.versus.restlet.DecisionSupport;


public class DecisionSupportServiceImpl implements DecisionSupportService {

	private final static ConcurrentMap<String, DecisionSupport> supportList = new ConcurrentHashMap<String, DecisionSupport>();
	
	@Override
	public void addDecisionSupport(DecisionSupport ds) {
		supportList.put(ds.getId(), ds);
	}

	@Override
	public DecisionSupport getDecisionSupport(String id) {
		return supportList.get(id);
	}

	@Override
	public Collection<DecisionSupport> listAll() {
		return supportList.values();
	}		

}