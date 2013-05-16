package edu.illinois.ncsa.versus.store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

public class InMemoryLinearIndexer implements IndexerProcessor{
	private final static ConcurrentMap<String, Indexer> indexerList = new ConcurrentHashMap<String, Indexer>();

	@Override
	public void addIndexer(Indexer in) {
		indexerList.put(in.getId(), in);

	}

	@Override
	public Indexer getIndexer(String id) {
		
		return indexerList.get(id);
	}
	
	@Override
	public void addDescriptor(String indexerId,Descriptor document) {
		indexerList.get(indexerId).getDescriptors().add(document);
		
	}

	@Override
	public void addDescriptor(String indexerId,Descriptor document, String id) {
			indexerList.get(indexerId).getDescriptors().add(document);
			indexerList.get(indexerId).getIdentifiers().add(id);
	}

	@Override
	public void build() {
				
	}

	@Override
	public List<SearchResult> query(String indexerId,Descriptor query) {
		
		//return indexerList.get(indexerId).query(query);
		List<SearchResult> sArray=new ArrayList<SearchResult>();
	  	SearchResult result=new SearchResult();
	  	List<Descriptor> des=indexerList.get(indexerId).getDescriptors();
		List<String> ids=indexerList.get(indexerId).getIdentifiers();
		Measure measure=indexerList.get(indexerId).getMeasure();
		for(int i=0;i<des.size();i++){
			   try {
				 result.setProximity(measure.compare(des.get(i), query));
				 result.setDocId(ids.get(i));
				sArray.add(result);
				result=new SearchResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			result.setProximity(measure.compare(query, query));
			 result.setDocId("0");
			sArray.add(result);
		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		return sArray;
	}

	@Override
	public List<SearchResult> query(String indexerId, Descriptor query, Measure measure) {
		List<SearchResult> sArray=new ArrayList<SearchResult>();
	  	SearchResult result=new SearchResult();
	  	List<Descriptor> des=indexerList.get(indexerId).getDescriptors();
		List<String> ids=indexerList.get(indexerId).getIdentifiers();
		for(int i=0;i<des.size();i++){
			   try {
				 result.setProximity(measure.compare(des.get(i), query));
				 result.setDocId(ids.get(i));
				sArray.add(result);
				result=new SearchResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			result.setProximity(measure.compare(query, query));
			 result.setDocId("0");
			sArray.add(result);
		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		return sArray;
    	}

	@Override
	public List<SearchResult> query(Descriptor query, int n) {
		
		return null;
	}

	@Override
	public boolean existIndexer(String id) {
		return indexerList.containsKey(id);
		
	}

	/*@Override
	public void setMeasure(String indexerId,Measure measure) {
		indexerList.get(indexerId).setMeasure(measure);
		
	}*/

	@Override
	public List<String> getIdentifiers(String indexerId) {
		
		return indexerList.get(indexerId).getIdentifiers();
	}

	@Override
	public List<Descriptor> getDescriptors(String indexerId) {
		
		return indexerList.get(indexerId).getDescriptors();
	}
	

}
