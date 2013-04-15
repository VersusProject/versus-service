package edu.illinois.ncsa.versus.store;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Inject;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

public class IndexerServiceImpl implements IndexerService{

//private final IndexerProcessor indexerList;
private final static ConcurrentMap<String, Indexer> indexerList = new ConcurrentHashMap<String, Indexer>();
	
	/*@Inject
	public IndexerServiceImpl(IndexerProcessor indexerProcessor){
      this.indexerList=indexerProcessor;	
    
	}*/
	/*public IndexerProcessor getIndexerProcessor(){
		return indexerList;
	}*/

	@Override
	public void addIndexer(Indexer in) {
		indexerList.put(in.getId(),in);

	}

	@Override
	public Indexer getIndexer(String id) {
		//return indexerList.getIndexer(id);
		return indexerList.get(id);
	}
	@Override
	public boolean existIndexer(String id) {
		
		if(indexerList.containsKey(id))
		//return indexerList.exist(id);
			return true;
		else
			return false;
	}
	
}
