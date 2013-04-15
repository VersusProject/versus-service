package edu.illinois.ncsa.versus.store;

import java.io.File;
import java.util.Collection;
import java.util.List;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.restlet.DecisionSupport;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

public interface IndexService {
	
	/* Index*/
	public void addIndex(Index in) ;
	public Index getIndex(String id);
    public Collection<Index> listAll(); 
    
    /*Indexer*/
   // public void addIndexer(Indexer in);
   // public Indexer getIndexer(String id);
    
    
    
    /*void addDescriptor(Descriptor document);
	void addDescriptor(Descriptor document, String id);
	void build();
	List<SearchResult> query(Descriptor query);
	List<SearchResult> query(Descriptor query,Measure measure);
	
	List<SearchResult> query(Descriptor query, int n);
	
	void setMeasure(Measure measure);
	
	 List<String> getIdentifiers();
	 public List<Descriptor> getDescriptors();*/

  }
