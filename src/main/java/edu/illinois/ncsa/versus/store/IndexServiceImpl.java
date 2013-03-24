package edu.illinois.ncsa.versus.store;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Inject;



import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.restlet.DecisionSupport;

public class IndexServiceImpl implements IndexService{

	//private final static ConcurrentMap<String, Index> indexList = new ConcurrentHashMap<String, Index>();
	
	
	private final IndexProcessor indexList;
	
	@Inject
	public IndexServiceImpl(IndexProcessor indexProcessor){
      this.indexList=indexProcessor;		
	}
			
	@Override
	public void addIndex(Index in) {
		indexList.addIndex(in);
		//indexList.put(in.getId(), in);
	}


	@Override
	public Index getIndex(String id) {
		return indexList.getIndex(id);
			//return indexList.get(id);
	}


	@Override
	public Collection<Index> listAll() {
		return indexList.listAll();
		//return indexList.values();
	}

	
	

}
