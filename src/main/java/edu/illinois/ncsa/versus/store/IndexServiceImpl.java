package edu.illinois.ncsa.versus.store;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.restlet.DecisionSupport;

public class IndexServiceImpl implements IndexService{

	private final static ConcurrentMap<String, Index> indexList = new ConcurrentHashMap<String, Index>();
	
			
	@Override
	public void addIndex(Index in) {
		// TODO Auto-generated method stub
		indexList.put(in.getId(), in);
	}


	@Override
	public Index getIndex(String id) {
		// TODO Auto-generated method stub
		return indexList.get(id);
	}


	@Override
	public Collection<Index> listAll() {
		// TODO Auto-generated method stub
		return indexList.values();
	}

	
	

}
