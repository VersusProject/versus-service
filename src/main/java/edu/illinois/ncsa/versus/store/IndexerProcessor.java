package edu.illinois.ncsa.versus.store;

import java.util.List;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;


public interface IndexerProcessor {
	
	void addDescriptor(String indexerId,Descriptor document);
	void addDescriptor(String indexerId,Descriptor document,String id);

	
	void build();

	List<SearchResult> query(String indexerId,Descriptor query);
	List<SearchResult> query(String indexerId,Descriptor query,Measure measure);
	
     
	
	List<SearchResult> query(Descriptor query, int n);
	
	//void setMeasure(String indexerId,Measure measure);
	
	List<String> getIdentifiers(String indexerId);
	public List<Descriptor> getDescriptors(String indexerId);
	
	 void addIndexer(Indexer in);
	Indexer getIndexer(String id);
	boolean existIndexer(String id);

}
